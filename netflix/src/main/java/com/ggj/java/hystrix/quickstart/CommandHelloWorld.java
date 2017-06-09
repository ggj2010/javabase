package com.ggj.java.hystrix.quickstart;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;


/**
 * HystrixCommand<String> 返回类型是string
 * @author:gaoguangjin
 * @date 2017/5/23 13:57
 */
@Slf4j
public class CommandHelloWorld extends HystrixCommand<String> {
	
	private final String name;
	
	public CommandHelloWorld(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		this.name = name;
	}
	
	@Override
	protected String run() throws Exception {
		return "Hello " + name + "!";
	}
}
