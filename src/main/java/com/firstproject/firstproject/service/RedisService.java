package com.firstproject.firstproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.firstproject.api.QuoteObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key,Class<T> entityClass) {
        try{
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(o.toString(), entityClass);
        } catch(Exception e) {
//            e.printStackTrace();
            log.error("Exception: ", e);
            return null;
        }
        return null;
    }

    public void set(String key, Object o, Long ttl) { /* ttl - time to live */
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o); // converting the incoming object as string so the StringRedisSerializer can convert it to serialized string
            redisTemplate.opsForValue().set(key, o, ttl, TimeUnit.SECONDS);
        } catch(Exception e) {
            log.error("Exception: ", e);
        }
    }
}
