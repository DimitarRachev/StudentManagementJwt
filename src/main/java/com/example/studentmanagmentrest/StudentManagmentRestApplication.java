package com.example.studentmanagmentrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebSecurity
@Configuration
public class StudentManagmentRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagmentRestApplication.class, args);
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/login").allowedOrigins("http://localhost:4200");
                registry.addMapping("/students").allowedOrigins("http//localhost:4200/students").allowedOrigins("http//localhost:4200");
            }
        };
    }
}
