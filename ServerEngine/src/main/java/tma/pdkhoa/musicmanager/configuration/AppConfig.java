package tma.pdkhoa.musicmanager.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.management.MalformedObjectNameException;
import javax.sql.DataSource;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tma.pdkhoa.musicmanager.api.dao.ModelDAO;
import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.api.service.MusicService;
import tma.pdkhoa.musicmanager.connection.JMSConnection;
import tma.pdkhoa.musicmanager.dao.ModelDAOImpl;
import tma.pdkhoa.musicmanager.service.MusicServiceImpl;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "tma.pdkhoa.musicmanager.configuration")
@PropertySource(value = { "classpath:properties/application.properties", "classpath:properties/jmx.properties" })
public class AppConfig {
    @Autowired
    private Environment environment;
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "tma.pdkhoa.musicmanager.model","tma.pdkhoa.musicmanager.api.entity"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
     }
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }
    
    @Bean
    @Autowired
    public ModelDAO getModelDAO(SessionFactory sessionFactory){
        ModelDAOImpl<MusicVO> modelDAO = new ModelDAOImpl<MusicVO>(MusicVO.class);
        modelDAO.setSessionFactory(sessionFactory);
        return modelDAO;
    }
    @Bean
    public JMSConnection getJMSConnection(){
        return JMSConnection.getInstance();
    }
    
    @Bean
    @Autowired
    public MusicService getMusicService(ModelDAO modelDAO, JMSConnection jmsConnection){
        MusicServiceImpl musicServiceImpl = new MusicServiceImpl();
        musicServiceImpl.setMusicDAO(modelDAO);
        musicServiceImpl.setJmsConnection(jmsConnection);
        return musicServiceImpl;
    }
    
    @Bean(name="rmiRegistry")
    public RmiRegistryFactoryBean rmiRegistryFactoryBean(){
        RmiRegistryFactoryBean rmiRegistryFactoryBean = new RmiRegistryFactoryBean();
        rmiRegistryFactoryBean.setPort(Integer.valueOf(environment.getRequiredProperty("jmx.rmi_port")));
        return rmiRegistryFactoryBean;
    }
    @Bean
    @DependsOn("rmiRegistry")
    public ConnectorServerFactoryBean connectorServerFactoryBean() throws MalformedObjectNameException{
        ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
        connectorServerFactoryBean.setObjectName("connector:name=MusicServiceConnector");
        connectorServerFactoryBean.setServiceUrl("service:jmx:rmi://127.0.0.1/jndi/rmi://127.0.0.1:10099/"+environment.getRequiredProperty("jmx.service_name"));
        return connectorServerFactoryBean;
    }
    
    @Bean
    @Lazy(false)
    @Autowired
    public MBeanExporter mBeanExporter(MusicService musicService){
        MBeanExporter mBeanExporter = new MBeanExporter();
        Map<String,Object> beans = new HashMap<String,Object>();
        beans.put(MusicService.SERVICE_NAME,musicService);
        mBeanExporter.setBeans(beans);
        return mBeanExporter;
    }
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto",environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }
}