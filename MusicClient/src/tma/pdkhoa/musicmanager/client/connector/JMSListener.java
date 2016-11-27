package tma.pdkhoa.musicmanager.client.connector;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;


public class JMSListener {
    final static Logger logger = Logger.getLogger(JMSListener.class);
    
    private static JMSListener instance;;
    
    private final String JNDI_FACTORY = org.jboss.naming.remote.client.InitialContextFactory.class.getName();
    private final String JMS_FACTORY = "jms/RemoteConnectionFactory";
    private final String TOPIC = "jms/topic/test";
    private final String jbossUrl = "http-remoting://127.0.0.1:8080";
    private final String username = "admin";
    private final String password = "admin";
    private Session session;
    private MessageConsumer consumer;
    private Topic topic;
    private Connection connection;
    private boolean isConnected;
    public boolean status = true;

    private JMSListener() {
        connect();
    }

    public synchronized static JMSListener getInstance() {
        if(instance == null){
            instance = new JMSListener();
        }
        return instance;
    }

    private InitialContext getInitialContext() throws NamingException {
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
            System.out.println("Create context");
            InitialContext ic = getInitialContext();
            System.out.println("Create Connection Factory");
            ConnectionFactory connectionFactory = (ConnectionFactory) ic.lookup(JMS_FACTORY);
            System.out.println("Create connection");
            connection = connectionFactory.createConnection();
            System.out.println("Create session");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Lookup Queue");
            topic = (Topic) ic.lookup(TOPIC);
            createConsumer();
            connection.start();
            System.out.println("finish");
            setConnected(true);
        }catch (Exception e){
            setConnected(false);
            logger.error("cannot connect JMS",e.getCause());
            ReconnectJob reconn = new ReconnectJob("Reconnect JMS");
            reconn.schedule(1000);
        }
    }

    private void createConsumer() {
        try{
            System.out.println("createConsumer");
            consumer = session.createConsumer(topic);
        }catch (JMSException e){
            e.printStackTrace();
        }
    }

    public String topicReceiveMessage() {
        try{
            TextMessage msg = (TextMessage) consumer.receive();
            System.out.println(msg.getText());
            return msg.getText();
        }catch (JMSException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
    
    private class ReconnectJob extends Job{
        public ReconnectJob(String name) {
            super(name);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            if(!JMSListener.getInstance().isConnected()) {
                JMSListener.getInstance().connect();
            }
            return Status.OK_STATUS;
        }
        
    }
        
}
