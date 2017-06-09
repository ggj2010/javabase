package com.ggj.java.hystrix.config;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;


/**
 * @author:gaoguangjin
 * @date 2017/5/23 16:25
 */
@Slf4j
public class CommandConfig extends HystrixCommand<String> {

    protected CommandConfig(Setter setter) {
        super(setter);
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(1000*2);
        log.info("执行 run");
        return "123";
    }
    @Override
    protected String getFallback() {
        return "fail";
    }
}
