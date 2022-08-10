package com.example.studentmanagmentrest.key.service;

import com.example.studentmanagmentrest.key.model.SecretKey;
import com.example.studentmanagmentrest.key.repository.KeyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@AllArgsConstructor
@Slf4j
@EnableScheduling
public class SecretKeyServiceImpl implements CommandLineRunner {


    KeyRepository repository;


    public static String KeyGen() { //key generator
        int len = 40;
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }


    @Override // initial secret key generation if db is empty.
    public void run(String... args) throws Exception {
        SecretKey secret = new SecretKey();
        if (repository.findAll().isEmpty()) {
            secret.setKey(KeyGen());
            repository.save(secret);
            secret.setKey(null);
        }
    }


    // @Scheduled(fixedDelay = 5000) //for test purpose only.
    @Scheduled(cron = "0 59 1 * * ?") //Gen new secret key every day at 01:59
    public void generateNew() {
        if (!repository.findAll().isEmpty()) {
            SecretKey secret = repository.findAll().get(0);
            secret.setKey(KeyGen());
            log.info(secret.getKey());
            repository.save(secret);
            secret.setKey(null);
            log.info(secret.getKey());

        }
    }


}
