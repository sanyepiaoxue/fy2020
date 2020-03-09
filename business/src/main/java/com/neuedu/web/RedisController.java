package com.neuedu.web;

import com.neuedu.common.RedisApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RedisController {
    @Autowired
    RedisApi redisApi;

    @RequestMapping("/redis/{key}/{value}")
    public String set(@PathVariable("key")String key,@PathVariable("value")String value){
        String value1 = redisApi.set(key,value);
        return value1;
    }

    @RequestMapping("/redis/hash")
    public Long setHash(){
        Long value = redisApi.hset("user:2:info","name","lucky");
        return value;
    }
}
