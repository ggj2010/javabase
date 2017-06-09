package com.ggj.java.hystrix.config;

import com.netflix.hystrix.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *  除了HystrixBadRequestException异常之外，所有从run()方法抛出的异常都算作失败，并触发降级getFallback()和断路器逻辑
 * @author:gaoguangjin
 * @date 2017/5/23 16:25
 */
@Slf4j
public class CircuitBreakerCommandConfig extends HystrixCommand<String> {
	
	int number;
	
	protected CircuitBreakerCommandConfig(Setter setter, int number) {
		super(setter);
		this.number = number;
	}
	
	@Override
	protected String run() throws Exception {
		log.info("执行 run number={}",number);
//		throw new RuntimeException("failure from CircuitBreakerCommandConfig");
//        Thread.sleep(1000*2);
//        return "result";
        throw new Exception("ddd");
//        throw new HystrixTimeoutException();
	}
	
	@Override
	protected String getFallback() {
		log.info("fallback number{}", number);
		return "fail";
	}
}
