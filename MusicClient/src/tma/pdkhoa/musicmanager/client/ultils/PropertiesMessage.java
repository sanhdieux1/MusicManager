package tma.pdkhoa.musicmanager.client.ultils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesMessage {
    private static PropertiesMessage instance = new PropertiesMessage();
    private static Properties properties;
    private PropertiesMessage(){
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("source/message.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("can't load source: message.properties");
        }
    }
    public static String getMessage(String message){
        return properties.getProperty(message);
    }
}
