package com.firstproject.firstproject.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled // disabling the test
    @Test
    public void testingRedis() {
        System.out.println("testing...");
        redisTemplate.opsForValue().set("email", "divysoni@email.com");
        Object email = redisTemplate.opsForValue().get("email");

        System.out.println("testing done");
    }

}
