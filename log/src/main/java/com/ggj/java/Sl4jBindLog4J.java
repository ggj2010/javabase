package com.ggj.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * sl4j绑定log4j
 *
 <dependency>
 <groupId>log4j</groupId>
 <artifactId>log4j</artifactId>
 <version>1.2.17</version>
 </dependency>
 <dependency>
 <groupId>org.slf4j</groupId>
 <artifactId>slf4j-log4j12</artifactId>
 </dependency>

 *
 * @author:gaoguangjin
 * @date:2018/5/25
 */
public class Sl4jBindLog4J {
    private static final Logger log= LoggerFactory.getLogger(Sl4jBindLog4J.class);
    public static void main(String[] args) {
        log.warn("a");
        log.debug("b");
        log.info("c");
        // 增加traceId 追踪请求的日志
        MDC.put("TRACE_ID", UUID.randomUUID().toString().replace("-", ""));
        log.error("e");
    }
}
