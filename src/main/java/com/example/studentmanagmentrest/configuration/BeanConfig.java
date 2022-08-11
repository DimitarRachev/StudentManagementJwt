package com.example.studentmanagmentrest.configuration;


import com.example.studentmanagmentrest.key.repository.KeyRepository;

import com.example.studentmanagmentrest.service.UserService;

import com.example.studentmanagmentrest.utility.EmailValidator;
import com.example.studentmanagmentrest.utility.TokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;
import java.util.Properties;


@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean
    public EmailValidator emailValidator() {
        return new EmailValidator();
    }
}
