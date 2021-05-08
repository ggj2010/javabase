package com.ggj.java.delayqueue.server.redis;

import redis.clients.jedis.Jedis;

/**
 * @author gaoguangjin
 */
public interface RedisCallBack<T> {
    public T doInRedis(Jedis jedis) throws Exception;
}
