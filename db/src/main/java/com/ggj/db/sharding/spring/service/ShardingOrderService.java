package com.ggj.db.sharding.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggj.db.sharding.spring.bean.ShardingOrder;
import com.ggj.db.sharding.spring.dao.ShardingOrderDao;

/**
 * @author:gaoguangjin
 * @date 2017/5/2 14:58
 */
@Service
@Transactional(readOnly = true)
public class ShardingOrderService {
	
	@Autowired
	private ShardingOrderDao dao;
	
	public List<ShardingOrder> select(ShardingOrder order) {
		return dao.select(order);
	}
	
	@Transactional(readOnly = false)
	public void insert(ShardingOrder order) {
		dao.insert(order);
	}
	@Transactional(readOnly = false)
	public void delete(ShardingOrder order) {
		dao.delete(order);
	}
}
