package com.ggj.java.hystrix.quickstart;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * Sample {@link HystrixCommand} that does not have a fallback implemented
 * so will "fail fast" when failures, rejections, short-circuiting etc occur.
 * 快速失败
 */
public class CommandThatFailsFast extends HystrixCommand<String> {
	
	private final boolean throwException;
	
	public CommandThatFailsFast(boolean throwException) {
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		this.throwException = throwException;
	}
	
	@Override
	protected String run() {
		if (throwException) {
			throw new RuntimeException("failure from CommandThatFailsFast");
		} else {
			return "success";
		}
	}
}
