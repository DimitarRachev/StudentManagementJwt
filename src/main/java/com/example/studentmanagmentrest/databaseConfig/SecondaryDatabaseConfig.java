package com.example.studentmanagmentrest.databaseConfig;

import com.example.studentmanagmentrest.key.repository.KeyRepository;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@AllArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "secondEntityManagerFactory",transactionManagerRef = "secondTransactionManager",
        basePackageClasses = KeyRepository.class)
public class SecondaryDatabaseConfig {




    @Bean(name = "secondDataSourceProps")
    @ConfigurationProperties("spring.datasource.second")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }


    @Bean(name = "secondData")
    public DataSource dataSource(@Qualifier("secondDataSourceProps") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }


    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryTwo(@Qualifier("secondData") DataSource dataSource,
                                                                          EntityManagerFactoryBuilder builder){
        return builder.dataSource(dataSource)
                .packages("com.example.studentmanagmentrest.key.model")
                .properties(propertiesMap())
                .persistenceUnit("dbTwo")
                .build();
    }


    @Bean(name="secondTransactionManager")
    public PlatformTransactionManager transactionManagerOne(@Qualifier("secondEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManager){
        return new JpaTransactionManager(entityManager.getObject());
    }

    public HashMap<String,Object> propertiesMap(){
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.enable_lazy_load_no_trans",true);
        properties.put("hibernate.show_sql",true);
        properties.put("hibernate.format_sql",false);
        return properties;
    }
}
