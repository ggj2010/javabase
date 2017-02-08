package com.ggj.java.lock;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 比如有服务器集群A,B,C 上个有个定时任务taskjob，每隔5分钟执行一次,
 * 假如我们的业务场景是 在同一时间只准某一台服务器执行就可以了。
 *
 * @author:gaoguangjin
 * @date 2016/12/15 11:15
 */
@Slf4j
public class TaskLock {
	
	
	static Jedis jedis;
	static {
		jedis = new Jedis("123.56.118.135", 6379);
		jedis.auth("gaoguangjin");
	}
	
	public static void main(String[] args) {
		// id 我们可以获取当前集群的ip地址，这样好区分下
		String id = UUID.randomUUID().toString();
		taskLockVersionOne(id);
	}
	
	/**
	 * 方案一的坏处：
	 * 假如在极端情况下，可能出现集群各个服务器同时执行到taskLockVersionOne，或者 doTask执行时间过长，
	 * 在redis事物还没提交的时候，会出现同时有多台服务器执行doTask。
	 * @param id
	 */
	private static void taskLockVersionOne(String id) {
		String key = "default_task_id";
		String value = jedis.get(key);
		// 用redis 事物是防止 在set成功之后 在执行doTask或者其他情况导致程序终止没有执行到transaction.expire（）
		// 导致单台机器一直占着锁会有单点事故
		Transaction transaction = null;
		try {
			transaction = jedis.multi();
			if (value == null) {
				transaction.set(key, id);
				doTask(id);
				// 设置过期是防止单点错误
				transaction.expire(key, 30);
			} else {
				if (value.equals(id)) {
					doTask(id);
				}
			}
		} catch (Exception e) {
			log.error("e" + e);
		} finally {
			transaction.exec();
		}
	}
	
	/**
	 * 方案二：
	 * 会重复两次回去jedis.get(key);
	 * 同时如果expire时间小于doTask时间，也会出现同时有两个任务执行doTask情况。
	 * @param id
	 */
	private static void taskLockVersionTwo(String id) {
		String key = "default_task_id";
		String value = jedis.get(key);
		// 用redis 事物是防止 在set成功之后 在执行doTask或者其他情况导致程序终止没有执行到transaction.expire（）
		// 导致单台机器一直占着锁会有单点事故
		Transaction transaction = null;
		try {
			transaction = jedis.multi();
			if (value == null) {
				transaction.set(key, id);
				transaction.expire(key, 30);
			}
		} catch (Exception e) {
			log.error("e" + e);
		} finally {
			transaction.exec();
		}
		value = jedis.get(key);
		if (value.equals(id)) {
			doTask(id);
		}
	}
	
	private static void taskLockVersionThree(String value) {
		String lockValue = "";
		String key = "default_task_id";
		try {
			// 如果不存在 等于拿到锁返回ok值 ，如果存在了就返回null
			String result = jedis.set(key, value, "NX", "EX", 60);
			log.info("value" + result);
			if (result != null && "ok".equals(result)) {
				// 拿到锁 处理其他逻辑
				lockValue = jedis.get(key);
				// 举个例子，一个客户端拿到了锁，被某个操作阻塞了很长时间，过了超时时间后自动释放了这个锁，然后这个客户端之后又尝试删除这个其实已经被其他客户端拿到的锁。
				// 所以单纯的用DEL指令有可能造成一个客户端删除了其他客户端的锁，
				// 用上面这个脚本可以保证每个客户单都用一个随机字符串’签名’了，这样每个锁就只能被获得锁的客户端删除了。
				doTask(value);
			}
		} catch (Exception e) {
			log.error("e" + e);
		} finally {
			if (lockValue != null && value.equals(lockValue)) {
				jedis.del(key);
			}
		}
	}
	
	private static void doTask(String id) {
		log.info("% ----执行任务-----", id);
	}
}
