package tma.pdkhoa.musicmanager.connection;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class JMSConnection {
    final static Logger logger = Logger.getLogger(JMSConnection.class);
    private final String JNDI_FACTORY = org.jboss.naming.remote.client.InitialContextFactory.class.getName();
    private final String JMS_FACTORY = "jms/RemoteConnectionFactory";
    private final String TOPIC = "jms/topic/test";
    private final String jbossUrl = "http-remoting://127.0.0.1:8080";
    private final String username = "admin";
    private final String password = "admin";
    private Session session;
    private Topic topic;
    private Connection connection;
    private MessageProducer publisher;

    public JMSConnection() {
        try {
            connect();
        } catch (NamingException | JMSException e) {
            logger.error("Cann't start JMS connection", e);
        }
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
//        env.put("jboss.naming.client.ejb.context", "false");
//        env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return new InitialContext(env);
    }

    public void connect() throws NamingException, JMSException {
        boolean loginfoEnable = logger.isInfoEnabled();
        loginfoEnable = true;
        System.out.println(loginfoEnable);
        if (loginfoEnable) {
            logger.info("Create context");
        }
        InitialContext ic = getInitialContext();
        if (loginfoEnable) {
            logger.info("Create Connection Factory");
        }
        ConnectionFactory connectionFactory = (ConnectionFactory) ic.lookup(JMS_FACTORY);
        if (loginfoEnable) {
            logger.info("Create connection");
        }
        connection = connectionFactory.createConnection();
        if (loginfoEnable) {
            logger.info("Create session");
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        if (loginfoEnable) {
            logger.info("Lookup Queue");
        }
        topic = (Topic) ic.lookup(TOPIC);
        createPublisher();
        connection.start();
    }

    private void createPublisher() {
        try {
            publisher = session.createProducer(topic);
        } catch (JMSException e) {
            logger.error("Cann't create publicsher", e);
        }
    }

    public void sendMessage() {
        try {
            TextMessage msg = session.createTextMessage();
            msg.setText("REFRESH_DATA");
            publisher.send(msg);
            logger.info("send: " + msg.getText());
        } catch (JMSException e) {
            logger.error("Cann't send message", e);
        }

    }
}
