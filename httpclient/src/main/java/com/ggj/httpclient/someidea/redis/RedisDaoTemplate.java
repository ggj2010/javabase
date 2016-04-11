package com.ggj.httpclient.someidea.redis;

import java.util.List;

import com.ggj.httpclient.someidea.redis.callback.RedisCallback;
import com.ggj.httpclient.someidea.redis.callback.TransactionCallBack;
import lombok.extern.slf4j.Slf4j;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;


/**
 * @ClassName:RedisDaoTemplate.java
 * @Description: <K, V>有可能返回式map类型，暂时没用到  ，<T>为泛型 
 * @author gaoguangjin
 * @Date 2015-9-1 下午3:53:49
 */
@Slf4j
public class RedisDaoTemplate {

	private static RedisDaoTemplate redisDaoTemplate=new RedisDaoTemplate();
	public static RedisDaoTemplate getRedisDaoTemplate() {
		return redisDaoTemplate;
	}

	private RedisDaoTemplate(){};


	public <T> T execute(RedisCallback<T> rc) {
		Jedis jedis = null;
		List<Object> object = null;
		try {
			// 用默认的db 0;
			jedis = RedisPool.getJedis();
			return rc.doInRedis(jedis);
		} catch (Exception e) {
			log.error("执行redis操作异常" + e.getLocalizedMessage());
			return null;
		}
		finally {
			RedisPool.returnJedis(jedis);
		}
	}
	
	public void execute(TransactionCallBack rc) {
		Jedis jedis = null;
		Transaction transaction;
		List<Object> object = null;
		try {
			jedis = RedisPool.getJedis();
			transaction = jedis.multi();
			rc.execute(transaction);
			object = transaction.exec();
		} catch (Exception e) {
			log.error("执行redis操作异常" + e.getLocalizedMessage());
		}
		finally {
			RedisPool.returnJedis(jedis);
		}
	}
}
