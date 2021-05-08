package com.ggj.java.delayqueue.server.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTemplate {
    private JedisPool redisPool;

    public <T> T exceute(RedisCallBack<T> callBack) throws Exception {
        try (Jedis jedis = redisPool.getResource()) {
            return callBack.doInRedis(jedis);
        } catch (Exception e) {
            throw e;
        }
    }

    private RedisTemplate() {
        this.redisPool = new RedisPool().getJedisPool();
    }

    public static RedisTemplate getRedisTemplate() {
        return SingletonHolder.redisTemplate;
    }

    private static class SingletonHolder {
        private static final RedisTemplate redisTemplate = new RedisTemplate();
    }
}
