package com.example.studentmanagmentrest.key.service;


import com.example.studentmanagmentrest.key.model.SecretKey;
import com.example.studentmanagmentrest.key.repository.KeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class SecretKeyServiceImpl implements CommandLineRunner {

    KeyRepository repository;


    public static String randomGenerator() {
        int len = 40;
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.findAll().isEmpty()) {
            SecretKey secret = new SecretKey();
            secret.setKey(randomGenerator());
            repository.save(secret);
            secret.setKey(null);
        }
    }
}
