package com.example.studentmanagmentrest.configuration;


import com.example.studentmanagmentrest.key.repository.KeyRepository;

import com.example.studentmanagmentrest.service.UserService;

import com.example.studentmanagmentrest.utility.TokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;


@Configuration
public class BeanConfig {
    private final UserService userService;

    public BeanConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public TokenGenerator tokenGenerator() {return new TokenGenerator(userService);}

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

}
