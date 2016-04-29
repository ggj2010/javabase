package com.ggj.encrypt.common.utils.redis.callback;

import redis.clients.jedis.Transaction;

/**
 * @ClassName:TransactionCallBack.java
 * @Description:   开启事务的jedis
 * @author gaoguangjin
 * @Date 2015-9-1 下午3:16:31
 */
public interface TransactionCallBack {
	
	void execute(Transaction t);
	
}
