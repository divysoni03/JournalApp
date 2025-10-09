package com.firstproject.firstproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {

        /* where RedisFactoryConnection is used to make and manage connection*/
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        /* then we can set key or value serializer */
        redisTemplate.setKeySerializer(new StringRedisSerializer()); /* where key datatype will be string so StringRedisSerializer*/
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
