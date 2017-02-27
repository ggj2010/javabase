package com.ggj.java.lottery;

import lombok.Getter;
import lombok.Setter;

/**
 * 奖品
 * @author:gaoguangjin
 * @date 2017/2/13 14:11
 */
@Getter
@Setter
public class Gift {
	
	private String id;
	
	private String name;

	// 概率
	private String probability;

	// 奖品数量
	private long giftNum;

	private double numberRange;
	
	public Gift(String id, String name, String probability, long giftNum) {
		this.id = id;
		this.name = name;
		this.probability = probability;
		this.giftNum = giftNum;
	}
}
