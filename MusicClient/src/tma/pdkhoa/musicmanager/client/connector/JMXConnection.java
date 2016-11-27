package tma.pdkhoa.musicmanager.client.connector;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.eclipse.core.runtime.Status;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import tma.pdkhoa.musicmanager.api.service.MusicService;

public class JMXConnection {
    private static Logger logger = Logger.getLogger(JMXConnection.class);
    private static JMXConnection instance = new JMXConnection();
    private static final String JMX_SERVER_STUB = "musicJMXConnection";
    private static final String JMX_SERVER_PORT = "10099";

    private MusicService musicService;

    private boolean isConnected;

    private long timeToReconnect = 1 * 10 * 1000;
    private JMXConnection() {
            connect();
            
        
    }

    public static JMXConnection getInstance() {
        return instance;
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public void connect() {
        try{
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + JMX_SERVER_PORT + "/" + JMX_SERVER_STUB);
            JMXConnector jmxc = JMXConnectorFactory.connect(url,null);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            Set<ObjectName> names = 
                    new TreeSet<ObjectName>(mbsc.queryNames(null, null));
                for (ObjectName name : names) {
                    if(name.toString().startsWith("bean"))
                    System.out.println("\tObjectName = " + name);
                }
            ObjectName objectName = new ObjectName(MusicService.SERVICE_NAME);
            musicService = (MusicService) JMX.newMBeanProxy(mbsc, objectName, MusicService.class, true);
            setConnected(true);
        }catch (Exception e){
            setConnected(false);
            logger.error("Cannot connect to JMX topic", e.getCause());
            JobReconnect reconn = new JobReconnect("reconnect JMX job");
            reconn.schedule(timeToReconnect);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    private class JobReconnect extends Job {

        public JobReconnect(String name) {
            super(name);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            if(!JMXConnection.getInstance().isConnected()){
                logger.warn("trying reconnect JMX");
                try{
                    JMXConnection.getInstance().connect();
                }catch (Exception e){
                    return Status.CANCEL_STATUS;
                }
            }
            logger.info("JMX is Connected");
            return Status.OK_STATUS;
        }

    }
}
