package com.ggj.encrypt.common.utils.redis;

import java.util.List;

import com.ggj.encrypt.common.utils.redis.callback.RedisCallback;
import com.ggj.encrypt.common.utils.redis.callback.TransactionCallBack;
import com.ggj.encrypt.configuration.RedisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;



/**
 * @ClassName:RedisDaoTemplate.java
 * @Description: <K, V>有可能返回式map类型，暂时没用到  ，<T>为泛型 
 * @author gaoguangjin
 * @Date 2015-9-1 下午3:53:49
 */
@Slf4j
@Service
public class RedisDaoTemplate {

	@Autowired
	private RedisPool redisPool;


	public <T> T execute(RedisCallback<T> rc) throws Exception {
		Jedis jedis = null;
		List<Object> object = null;
		try {
			// 用默认的db 0;
			jedis = redisPool.getJedis();
			return rc.doInRedis(jedis);
		}
		finally {
			redisPool.returnJedis(jedis);
		}
	}
	
	public void execute(TransactionCallBack rc) throws Exception {
		Jedis jedis = null;
		Transaction transaction;
		List<Object> object = null;
		try {
			jedis = redisPool.getJedis();
			transaction = jedis.multi();
			rc.execute(transaction);
			object = transaction.exec();
		}
		finally {
			redisPool.returnJedis(jedis);
		}
	}
}
