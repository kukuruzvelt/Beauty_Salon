package task.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import task.models.entity.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(
                new String[]{"com.baeldung.hibernate.bootstrap.model"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan(User.class.getPackage().getName());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();
        InputStream is;
        try {
            is = HibernateConfig.class.getClassLoader().getResourceAsStream("database_info.properties");
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataSource.setUrl(prop.getProperty("connection_url"));
        dataSource.setUsername(prop.getProperty("username"));
        dataSource.setPassword(prop.getProperty("password"));


        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");

        return hibernateProperties;
    }
}