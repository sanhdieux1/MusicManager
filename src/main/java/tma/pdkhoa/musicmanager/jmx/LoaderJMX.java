package tma.pdkhoa.musicmanager.jmx;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import tma.pdkhoa.musicmanager.configuration.AppConfig;

public class LoaderJMX implements LoaderJMXMBean {
    AbstractApplicationContext context;
    private String message = "Sorry no message today";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoaderJMX() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
//        context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
    }

    // The lifecycle
    public void start() throws Exception {
        System.out.println(">>>>Starting with message=" + message);
    }

    public void stop() throws Exception {
        System.out.println(">>>>Stopping with message=" + message);
    }

    public void printMessage() {
        System.out.println(message);
    }
}
