package com.ggj.db.sharding.spring.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:gaoguangjin
 * @date 2017/5/2 14:31
 */
@Getter
@Setter
public class ShardingOrder {
	
	private int userId;
	
	private int orderId;
	
	private String status;
	
	@Override
	public String toString() {
		return "ShardingOrder{" + "userId=" + userId + ", orderId=" + orderId + ", status='" + status + '\'' + '}';
	}
}
