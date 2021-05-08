package com.ggj.java.delayqueue.server.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author gaoguangjin
 */
public class RedisPool {

    private JedisPool jedisPool;
    public final static int threadSize = 1000;


    public RedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMaxIdle(100);
        poolConfig.setMaxTotal(threadSize);
        poolConfig.setMinIdle(20);
        poolConfig.setMaxWaitMillis(5000);
        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 2000);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
