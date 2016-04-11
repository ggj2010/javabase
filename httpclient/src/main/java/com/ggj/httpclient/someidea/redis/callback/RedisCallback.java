package com.ggj.httpclient.someidea.redis.callback;

import redis.clients.jedis.Jedis;

/**
 * @ClassName:RedisCallback.java
 * @Description: jedis   
 * @author gaoguangjin
 * @Date 2015-9-1 下午3:17:08
 */
public interface RedisCallback<T> {
	T doInRedis(Jedis jedis);
}
