package com.neuedu.web;

import com.neuedu.common.RedisApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
public class RedisController {
    @Autowired
    RedisApi redisApi;

    @RequestMapping("/redis")
    public String set(){
        String value = redisApi.set("neusoft","hello");
        return value;
    }

    @RequestMapping("/redis/hash")
    public Long setHash(){
        Long value = redisApi.hset("user:2:info","name","lucky");
        return value;
    }
}
