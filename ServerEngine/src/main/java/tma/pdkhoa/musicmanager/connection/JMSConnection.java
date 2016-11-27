package tma.pdkhoa.musicmanager.connection;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import tma.pdkhoa.musicmanager.api.service.MusicException;

public class JMSConnection {
    final static Logger logger = Logger.getLogger(JMSConnection.class);
    private static JMSConnection instance;
    private final String JNDI_FACTORY = org.jboss.naming.remote.client.InitialContextFactory.class.getName();
    private final String JMS_FACTORY = "jms/RemoteConnectionFactory";
    private final String TOPIC = "jms/topic/test";
    private final String jbossUrl = "http-remoting://127.0.0.1:8080";
    private final String username = "admin";
    private final String password = "admin";
    private TopicSession session;
    private Topic topic;
    private TopicConnection connection;
    private TopicPublisher publisher;
    private boolean isConnected;

    private JMSConnection() {
        connect();
    }

    public synchronized static JMSConnection getInstance() {
        if(instance == null){
            instance = new JMSConnection();
            JobDetail job =  JobBuilder.newJob(ReconnectJob.class).withIdentity(ReconnectJob.jobName).build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("JMSTrigger")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(30)
                            .repeatForever())
                    .build();
            Scheduler scheduler;
            try{
                scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.scheduleJob(job, trigger);
                scheduler.start();
            }catch (SchedulerException e){
                logger.error("Cannot start JMS reconnect job ", e.getCause());
            }
            
        }
        return instance;
    }

    private InitialContext getInitialContext() throws NamingException {
        logger.info(Context.INITIAL_CONTEXT_FACTORY + " : " + JNDI_FACTORY);
        logger.info(Context.PROVIDER_URL + " : " + jbossUrl);
        logger.info(Context.SECURITY_PRINCIPAL + " : " + username);
        logger.info(Context.SECURITY_CREDENTIALS + " : " + password);
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, jbossUrl);
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        // env.put("jboss.naming.client.ejb.context", "false");
        // env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return new InitialContext(env);
    }

    public void connect() {
        try{
            boolean loginfoEnable = logger.isInfoEnabled();
            loginfoEnable = true;
            System.out.println(loginfoEnable);
            if(loginfoEnable){
                logger.info("Create context");
            }
            InitialContext ic = getInitialContext();
            if(loginfoEnable){
                logger.info("Create Connection Factory");
            }
            HornetQConnectionFactory connectionFactory = (HornetQConnectionFactory) ic.lookup(JMS_FACTORY);
            if(loginfoEnable){
                logger.info("Create connection");
            }
            connection = connectionFactory.createTopicConnection();
            if(loginfoEnable){
                logger.info("Create session");
            }
            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            if(loginfoEnable){
                logger.info("Lookup Queue");
            }
            topic = (Topic) ic.lookup(TOPIC);
            createPublisher();
            
            connection.start();
            setConnected(true);
        }catch (NamingException | JMSException e){
            logger.error("Cann't start JMS connection", e);
            setConnected(false);
        }
    }

    private void createPublisher() throws JMSException {
            publisher = session.createPublisher(topic);

    }

    public void sendMessage() throws MusicException {
        try{
            TextMessage msg = session.createTextMessage();
            msg.setText("REFRESH_DATA");
            publisher.publish(msg);
            logger.info("send: " + msg.getText());
        }catch (JMSException e){
            logger.error("Cann't send message", e);
            throw new MusicException("JMSException", "GENERIC", "can't send message");
        }

    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public static class ReconnectJob implements Job {
        public static final String jobName = "JMSReconnectJob";

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            if(!JMSConnection.getInstance().isConnected){
                JMSConnection.getInstance().connect();
            }
        }

    }

}
