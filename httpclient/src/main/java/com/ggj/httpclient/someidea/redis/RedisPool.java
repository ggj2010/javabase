package com.ggj.httpclient.someidea.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/7 22:57
 */
public class RedisPool {
	private static String host;
	private static String port;
	private static String timeout;
	private static String passwords;
	
	private static String maxtotal;
	private static String maxidle;
	private static String minidle;
	private static String maxwaitmillis;
	private static String testonborrow;
	private static String testwhileidle;
	
	private static JedisPool jedisPool;
	
	static {
		InputStream is;
		try {
			Properties props = new Properties();
			is = RedisPool.class.getResourceAsStream("/redis.properties");
			props.load(is);
			host = props.getProperty("host");
			port = props.getProperty("port");
			timeout = props.getProperty("timeout");
			passwords = props.getProperty("passwords");
			maxtotal = props.getProperty("maxtotal");
			maxidle = props.getProperty("maxidle");
			minidle = props.getProperty("minidle");
			maxwaitmillis = props.getProperty("maxwaitmillis");
			testonborrow = props.getProperty("testonborrow");
			testwhileidle = props.getProperty("testwhileidle");
			
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setTestOnBorrow(testwhileidle.equals("true") ? true : false);
			poolConfig.setTestWhileIdle(testonborrow.equals("true") ? true : false);
			poolConfig.setMaxIdle(Integer.parseInt(maxidle));
			poolConfig.setMaxTotal(Integer.parseInt(maxtotal));
			poolConfig.setMinIdle(Integer.parseInt(minidle));
			poolConfig.setMaxWaitMillis(Integer.parseInt(maxwaitmillis));
			
			jedisPool = new JedisPool(poolConfig, host, Integer.parseInt(port), Integer.parseInt(timeout), passwords);
			
		} catch (IOException e) {
			System.out.println("初始化redis连接池失败！");
		}
	}
	
	public static JedisPool getJedisPool() {
		return jedisPool;
	}
	
	public static Jedis getJedis() {
		if (jedisPool != null)
			return jedisPool.getResource();
		return null;
	}
	
	public static void returnJedis(Jedis jedis) {
		if (jedis != null)
			jedisPool.returnResource(jedis);
	}
	
}
