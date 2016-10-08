package com.ggj.java.firstdemo.com.ggj.java.bean;


import lombok.Getter;
import lombok.Setter;

/**
 * @author:gaoguangjin
 * @date 2016/9/20 16:19
 */

@Getter
@Setter
public class User {
	
	private long id;
	
	private String first_name;
	
	private String last_name;
	
	private int age;
	
	private String about;
	
	private String[] interests;
}
