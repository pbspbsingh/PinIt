package com.pbs.app.component.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Log4jConfigurer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class Configurer {

	@Autowired
	private Environment env;

	@Bean(name = "log4jInitialization")
	public MethodInvokingFactoryBean createMethodInvokingFactoryBean() {
		final MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
		factoryBean.setTargetClass(Log4jConfigurer.class);
		factoryBean.setTargetMethod("initLogging");
		factoryBean.setArguments(new Object[] { "classpath:log4j.properties" });
		return factoryBean;
	}

	@Bean
	public DataSource dataSource() {
		final BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName(env.getRequiredProperty("db.driver"));
		datasource.setUrl(env.getRequiredProperty("db.url"));
		datasource.setUsername(env.getRequiredProperty("db.username"));
		datasource.setPassword(env.getRequiredProperty("db.password"));
		return datasource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactoryBean(final DataSource ds) {
		final LocalSessionFactoryBean sfBean = new LocalSessionFactoryBean();
		sfBean.setDataSource(ds);
		sfBean.setPackagesToScan("com.pbs.app.entity");
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
		sfBean.setHibernateProperties(hibernateProperties);
		return sfBean;
	}

	@Bean
	public HibernateTransactionManager transactionManager(final SessionFactory sessionFactory) {
		final HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
}
