package padmin.app;

import java.io.File;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "padmin")
public class SpringConfiguration {
  private static final Logger log = LoggerFactory.getLogger(SpringConfiguration.class);

  @Bean
  public static PropertyOverrideConfigurer propertyOverride() {
    final PropertyOverrideConfigurer properties = new PropertyOverrideConfigurer();

    final String config = System.getProperty("config");
    if (config == null || config.isEmpty()) {
      log.error("No 'config' system property detected - padmin cannot start up.");
      throw new RuntimeException("No 'config' system property detected - padmin cannot start up.");
    }

    final File configFile = new File(config, "padmin.properties");
    if (!configFile.exists() || !configFile.canRead()) {
      log.error("Attempted to load the padmin configuration file from {}/padmin.properties, but either it does not exist or is not readable.", config);
      throw new RuntimeException(String.format("Attempted to load the padmin configuration file from %s/padmin.properties, but either it does not exist or is not readable.", config));
    }

    Resource location = new FileSystemResource(configFile);
    properties.setLocation(location);

    return properties;
  }

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

    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager() throws Exception {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }
}