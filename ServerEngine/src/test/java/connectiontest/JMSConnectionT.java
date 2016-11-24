/*package connectiontest;

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

import tma.pdkhoa.musicmanager.connection.JMSConnection;

public class JMSConnectionT {
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

    public JMSConnectionT() {
        try {
            connect();
        } catch (NamingException | JMSException e) {
            System.out.println("Dy me loi o day");
            e.printStackTrace();
        }
    }

    private InitialContext getInitialContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        if(logger.isDebugEnabled()){
            logger.debug("This is debug : " +Context.INITIAL_CONTEXT_FACTORY + ":" + JNDI_FACTORY);
        }
        if(logger.isInfoEnabled()){
            logger.info("This is info : " +Context.PROVIDER_URL + ":" + jbossUrl);
        }
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, jbossUrl);
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
//         env.put("jboss.naming.client.ejb.context", "false");
//         env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return new InitialContext(env);
    }

    public void connect() throws NamingException, JMSException {
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
        createPublisher();
        System.out.println("finish");
        connection.start();
    }

    private void createPublisher() {
        try {
            System.out.println("create publicsher");
            publisher = session.createProducer(topic);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            TextMessage msg = session.createTextMessage();
            msg.setText("REFRESH_DATA");
            publisher.send(msg);
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JMSConnectionT jms = new JMSConnectionT();
        System.out.println("success");
    }
}
*/