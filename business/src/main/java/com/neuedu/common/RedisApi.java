package com.neuedu.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 封装redis中字符串、哈希、列表、集合、有序集合API
 * */

@Component
public class RedisApi {

    @Autowired
    //private JedisPool jedisPool;
    private JedisSentinelPool jedisPool;

    /**
     * 字符串
     * 添加key value
     */

    public String set(String key,String value){

        HostAndPort hostAndPort = jedisPool.getCurrentHostMaster();
        System.out.println("====================");
        System.out.println(hostAndPort.getHost());
        System.out.println(hostAndPort.getPort());


        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key,value);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     * 根据key 获取value
     */
    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     * key存在，设置不成功
     * key不存在，设置成功
     */
    public Long setNX(String key,String value){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.setnx(key,value);
        jedis.close();
        return result;
    }

    /**
     * 字符串原子性
     * 先get，再set
     */
    public String getSet(String key,String value){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.getSet(key,value);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     * 为key设置过期时间
     */
    public Long expire(String key,int timeout){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key,timeout);
        jedis.close();
        return result;
    }

    //删除
    public Long remove(String key){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }



    /**
     * 字符串
     * 查看key剩余时间
     */

    public Long ttl(String key){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.ttl(key);
        jedis.close();
        return result;
    }

    /**
     * 字符串
     * 在设置key，value时，为key指定过期时间
     */
    public String setEx(String key,Integer timeout,String value){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.setex(key,timeout,value);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 设置key、field、value
     */
    public Long hset(String key,String field,String value){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key,field,value);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 批量设置key、field、value
     */
    public String hset(String key, Map<String,String> map){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hmset(key,map);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key、field查看value
     */
    public String hget(String key,String field){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key,field);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的field、value
     */
    public Map<String,String> hgetAll(String key){
        Jedis jedis =jedisPool.getResource();
        Map<String,String> result = jedis.hgetAll(key);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的field
     */
    public Set<String> hgetAllField(String key){
        Jedis jedis = jedisPool.getResource();
        Set<String> result = jedis.hkeys(key);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 根据key，查看所有的value
     */
    public List<String> hgetAllVals(String key){
        Jedis jedis = jedisPool.getResource();
        List<String> result = jedis.hvals(key);
        jedis.close();
        return result;
    }

    /**
     * 哈希结构-api封装
     * 计数器
     */
    public Long hgetAllVals(String key,String field,Long incr){
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hincrBy(key,field,incr);
        jedis.close();
        return result;
    }
}
