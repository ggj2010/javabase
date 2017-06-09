package com.ggj.java.hystrix.config;

import com.netflix.hystrix.HystrixCommand;

import lombok.extern.slf4j.Slf4j;


/**
 * @author:gaoguangjin
 * @date 2017/5/23 16:25
 */
@Slf4j
public class FallbackCommandConfig extends HystrixCommand<String> {

    protected FallbackCommandConfig(Setter setter) {
        super(setter);
    }

    @Override
    protected String run() throws Exception {
        log.info("执行 run");
        throw new RuntimeException("failure from CommandThatFailsFast");
    }
    @Override
    protected String getFallback() {
        log.info("执行 getFallback");
        return "fail";
    }
}
