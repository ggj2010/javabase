/**
 * Copyright 2012 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ggj.java.hystrix.quickstart;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 抛出异常throw new RuntimeException("this command always fails");
 * 调用fallback方法
 *
 */
public class CommandHelloFailure extends HystrixCommand<String> {
	
	private final String name;
	
	public CommandHelloFailure(String name) {
		super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
		this.name = name;
	}
	
	@Override
	protected String run() {
		throw new RuntimeException("this command always fails");
	}
	
	@Override
	protected String getFallback() {
		return "Hello Failure " + name + "!";
	}
}
