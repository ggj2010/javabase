package com.ggj.encrypt.modules.test.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @author:gaoguangjin
 * @date 2016/5/30 16:33
 */
@Data
public class City implements Serializable {
	private static final long serialVersionUID = 3726296677244538289L;
	private Long id;
	private String name;
	private String state;
	private String country;
}
