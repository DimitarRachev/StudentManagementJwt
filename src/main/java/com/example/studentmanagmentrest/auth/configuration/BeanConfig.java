package com.example.studentmanagmentrest.auth.configuration;

import com.example.studentmanagmentrest.service.UserService;
import com.example.studentmanagmentrest.utility.TokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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

}
