package com.quickpass.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Configuration JPA dédiée aux tests d’intégration.
 * <p>
 * Isolée du contexte principal, cette config garantit que les entités et repositories
 * sont correctement scannés et que la base H2 est initialisée proprement.
 */
@TestConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = "com.quickpass.signup.domain.model")
@EnableJpaRepositories(
        basePackages = "com.quickpass.signup.domain.repo",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Test.*")
)
public class TestJpaConfig {

    @Autowired
    private Environment env;

    /**
     * Fournit la factory JPA configurée pour les tests (base H2 en mémoire).
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.quickpass.signup.domain.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    /**
     * Fournit un TransactionManager pour les tests d’intégration.
     */
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    /**
     * Propriétés Hibernate adaptées aux tests (H2 + mode MySQL simulé).
     */
    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.use_sql_comments", "true");
        props.put("hibernate.jdbc.batch_size", "30");
        props.put("hibernate.order_inserts", "true");
        props.put("hibernate.order_updates", "true");
        return props;
    }
}
