package com.example.studentmanagmentrest.key.service;

import com.example.studentmanagmentrest.key.model.SecretKey;
import com.example.studentmanagmentrest.key.repository.KeyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
@EnableScheduling
public class SecretKeyServiceImpl implements CommandLineRunner {


    KeyRepository repository;


    public static String KeyGen() { //key generator
        return RandomStringUtils.randomAlphanumeric(40);
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


   //  @Scheduled(fixedDelay = 1000) //for test purpose only.
    @Scheduled(cron = "0 59 1 * * ?") //Gen new secret key every day at 01:59
    public void generateNew() {
        if (!repository.findAll().isEmpty()) {
            SecretKey secret = repository.findAll().get(0);
            secret.setKey(KeyGen());
//          log.info(secret.getKey()); //debugging
            repository.save(secret);
            secret.setKey(null);
//          log.info(secret.getKey());  //debugging

        }
    }


}
