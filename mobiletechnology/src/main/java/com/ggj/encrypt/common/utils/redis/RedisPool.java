package com.ggj.encrypt.common.utils.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ggj.encrypt.common.utils.DesUtil;
import com.ggj.encrypt.configuration.RedisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/7 22:57
 */
@Component
@Slf4j
public class RedisPool implements InitializingBean {
	@Autowired
	private RedisConfiguration redisConfiguration;
	
	private  JedisPool jedisPool;

	public  JedisPool getJedisPool() {
		return jedisPool;
	}
	
	public  Jedis getJedis() throws  Exception{
		if (jedisPool != null)
			return jedisPool.getResource();
		log.error("获取redis连接池失败，jedisPool为空");
		throw new Exception("获取redis连接池失败，jedisPool为空");
	}
	
	public  void returnJedis(Jedis jedis) {
		if (jedis != null)
			jedis.close();
		//log.info("jedis.close()：回收连接池");
		// jedis 2.8弃用了
		// jedisPool.returnResource(jedis);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setTestOnBorrow(redisConfiguration.getTestonborrow().equals("true") ? true : false);
			poolConfig.setTestWhileIdle(redisConfiguration.getTestwhileidle().equals("true") ? true : false);
			poolConfig.setMaxIdle(Integer.parseInt(redisConfiguration.getMaxidle()));
			poolConfig.setMaxTotal(Integer.parseInt(redisConfiguration.getMaxtotal()));
			poolConfig.setMinIdle(Integer.parseInt(redisConfiguration.getMinidle()));
			poolConfig.setMaxWaitMillis(Integer.parseInt(redisConfiguration.getMaxwaitmillis()));
			jedisPool = new JedisPool(poolConfig, redisConfiguration.getHost(), Integer.parseInt(redisConfiguration.getPort()),
					Integer.parseInt(redisConfiguration.getTimeout()), DesUtil.decrypt(redisConfiguration.getPasswords(), DesUtil.KEY_STR));
		} catch (IOException e) {
			log.error("初始化redis连接池失败！");
		}
	}
}
