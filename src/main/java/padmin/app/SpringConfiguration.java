package padmin.app;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="padmin")
public class SpringConfiguration {
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
    final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setBeanName("entityManagerFactory");
    emf.setPackagesToScan(new String[] { "padmin" });

    emf.setDataSource(dataSource());

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(true);
    jpaVendorAdapter.setDatabase(Database.POSTGRESQL);
    emf.setJpaVendorAdapter(jpaVendorAdapter);

    return emf;
  }

  @Bean
  public DataSource dataSource() {
    final ComboPooledDataSource dataSource = new ComboPooledDataSource();
    try {
      dataSource.setDriverClass("org.postgresql.Driver");
    } catch (Exception ex) {
      throw new RuntimeException("Could not set JDBC driver class.", ex);
    }
    dataSource.setJdbcUrl("jdbc:postgresql://192.168.1.8:5433/powerdns");
    dataSource.setUser("powerdns");
    dataSource.setPassword("powerdns2k10^^");
    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager() throws Exception {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }
}