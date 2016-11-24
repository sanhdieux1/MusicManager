package tma.pdkhoa.musicmanager.client.connector;

import java.io.IOException;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import tma.pdkhoa.musicmanager.api.service.MusicService;

public class JMXConnection {

    private static JMXConnection instance = new JMXConnection();

    private static final String JMX_SERVER_PORT = "10099";

    private static final String JMX_SERVER_STUB = "musicJMXConnection";

    private MusicService musicService;

    private boolean isConnected;
    
    private JMXConnection() {
        try {
            connect();
            setConnected(true);
        } catch (MalformedObjectNameException e) {
            setConnected(false);
            e.printStackTrace();
        } catch (IOException e) {
            setConnected(false);
            e.printStackTrace();
        }
    }

    public static JMXConnection getInstance() {
        return instance;
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public void connect() throws IOException, MalformedObjectNameException {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + JMX_SERVER_PORT + "/" + JMX_SERVER_STUB);
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        ObjectName objectName = new ObjectName("bean:name=musicService");
        musicService = (MusicService) JMX.newMBeanProxy(mbsc, objectName, MusicService.class, true);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

}
