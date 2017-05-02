package com.ggj.db.sharding.spring.dao;

import java.util.List;

import com.ggj.db.sharding.spring.bean.ShardingOrder;

/**
 * @author:gaoguangjin
 * @date 2017/2/22 14:23
 */
public interface ShardingOrderDao {
	
	void insert(ShardingOrder order);
	
	List<ShardingOrder> select(ShardingOrder shardingOrder);
	
	void delete(ShardingOrder order);
}
