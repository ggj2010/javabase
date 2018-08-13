package com.ggj.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * sl4j转换成log4j2
 * @author:gaoguangjin
 * @date:2018/5/25
 */
public class Sl4jToLog4j2 {

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
