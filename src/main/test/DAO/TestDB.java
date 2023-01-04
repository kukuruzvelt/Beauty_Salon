package DAO;

import java.io.IOException;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import task.models.entity.User;

public class TestDB {
  private static final LocalSessionFactoryBean localSessionFactoryBean;
  private static final String testDate = "2000-11-20";

  static {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Europe/Kiev");
    dataSource.setUsername("root");
    dataSource.setPassword("root");

    localSessionFactoryBean = new LocalSessionFactoryBean();

    localSessionFactoryBean.setDataSource(dataSource);
    localSessionFactoryBean.setPackagesToScan(User.class.getPackage().getName());
    try {
      localSessionFactoryBean.afterPropertiesSet();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static SessionFactory getSessionFactory() {
    return localSessionFactoryBean.getObject();
  }

  public static String getTestDate(){
    return testDate;
  }

}
