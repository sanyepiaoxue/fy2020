package com.neuedu.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
public class RedisController {
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/redis")
    public String set(){
        Jedis jedis = jedisPool.getResource();
        jedis.set("neuedu","fy2020");
        String value = jedis.get("neuedu");
        jedisPool.returnResource(jedis);
        return value;
    }
}
