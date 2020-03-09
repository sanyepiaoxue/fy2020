package com.neuedu.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisConfig {

    @Value("${redis.maxTotal}")
    private Integer maxTotal;
    @Value("${redis.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.minIdle}")
    private Integer minIdle;
    @Value("${redis.blockWhenExhausted}")
    private boolean blockWhenExhausted;
    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn}")
    private boolean testOnReturn;
    @Value("${redis.jmxEnabled}")
    private boolean jmxEnabled;

    @Value("${redis.redisHost}")
    private String redisHost;
    @Value("${redis.redisPort}")
    private Integer redisPort;
    @Value("${redis.redisPassword}")
    private String redisPass;
    @Value("${redis.timeout}")
    private Integer timeout;

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setBlockWhenExhausted(blockWhenExhausted);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setJmxEnabled(jmxEnabled);
        return config;
    }


    @Bean
    public JedisSentinelPool jedisSentinelPool(){

        /**
         * 内部本质还是去连接master主机：
         * 参数masterName表示master名称，
         * sentinelSet表示三个哨兵的IP地址和端口号，
         * genericObjectPoolConfig()：连接池的配置
         * timeout：超时时间
         * redisPass：主节点密码
         */

        Set<String> sentinelSet = new HashSet<>();
        sentinelSet.add("182.92.222.63:26379");
        sentinelSet.add("182.92.222.63:26380");
        sentinelSet.add("182.92.222.63:26381");

        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster",
                sentinelSet,genericObjectPoolConfig(),timeout,redisPass);
        return jedisSentinelPool;
    }
}
