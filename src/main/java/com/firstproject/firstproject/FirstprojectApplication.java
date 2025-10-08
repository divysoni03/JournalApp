package com.firstproject.firstproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/*we have written this annotation,
    this contains @Configuration annotation too,
    so every function that we write in this class will be treated as bean that's why
    we made transaction thing here
*/
@SpringBootApplication
@EnableTransactionManagement // searches for transaction operations
@EnableScheduling // to enable scheduling, so the userScheduler file runs on its on w.r.t cron expression
public class FirstprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstprojectApplication.class, args);
    }

    /*Here -> we make a function which we will use to implement the transaction property
    * for this we have two component one platformTransactionManager(interface), MongoTransactionManager(implementation)
    * where platformTransactionManager is just an Interface used to implement the transaction properties but behind the hood actually the real work is done by MongoTransactionManager
    * so we are using platformTransactionManager but that is actually using MongoTransactionManager behind the scene*/
    @Bean
    public PlatformTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
    /* so we are returning an instance/bean/objectInstance so we can use that to implement transaction features
       in any function or class where MongoDatabaseFactory helps to connect to database
    */


    // making a IOC bean of restTemplate , returning an instance of restTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}