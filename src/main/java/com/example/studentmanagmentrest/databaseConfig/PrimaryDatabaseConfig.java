package com.example.studentmanagmentrest.databaseConfig;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@AllArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.studentmanagmentrest.repository")
public class PrimaryDatabaseConfig {



    @Primary
    @Bean(name = "firstDataSourceProps")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "firstData")
    public DataSource dataSource(@Qualifier("firstDataSourceProps") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryOne(@Qualifier("firstData") DataSource dataSource,

                                                                          EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .packages("com.example.studentmanagmentrest.model.entity")
                .properties(propertiesMap())
                .persistenceUnit("dbOne")
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    @ConfigurationProperties(prefix = "spring.jpa")
    public PlatformTransactionManager transactionManagerOne(@Qualifier("entityManagerFactory") EntityManagerFactory entityManager) {
        return new JpaTransactionManager(entityManager);
    }

    public  HashMap<String,Object> propertiesMap(){
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.enable_lazy_load_no_trans",true);
        properties.put("hibernate.show_sql",true);
        properties.put("hibernate.format_sql",true);
        return properties;
    }

}
