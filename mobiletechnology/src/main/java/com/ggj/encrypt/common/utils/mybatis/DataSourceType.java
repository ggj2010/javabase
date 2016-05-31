package com.ggj.encrypt.common.utils.mybatis;

import lombok.Getter;

/**
 * @author:gaoguangjin
 * @date 2016/5/30 14:36
 */
public enum DataSourceType {
	read("read", "从库"), write("write", "主库");
	@Getter
	private String type;
	@Getter
	private String name;
	
	DataSourceType(String type, String name) {
		this.type = type;
		this.name = name;
	}
}
