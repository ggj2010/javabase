package com.ggj.java;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * 最基础的logback
 *
 * 直接引入
 * <dependency>
 <groupId>ch.qos.logback</groupId>
 <artifactId>logback-classic</artifactId>
 </dependency>
 *  sl4j绑定logback
 * @author:gaoguangjin
 * @date:2018/5/25
 */
public class Sl4jBindLogBack {
    private static final Logger log= LoggerFactory.getLogger(Sl4jBindLogBack.class);
    public static void main(String[] args) {
        log.warn("a");
        log.debug("b");
        log.info("c");
        // 增加traceId 追踪请求的日志
        MDC.put("TRACE_ID", UUID.randomUUID().toString().replace("-", ""));
        log.error("e");
    }
}
