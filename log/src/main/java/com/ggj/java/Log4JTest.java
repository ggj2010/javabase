package com.ggj.java;


import org.apache.log4j.Logger;

/**
 * 最基础的log4j
 * 直接引入
 * <dependency>
 <groupId>log4j</groupId>
 <artifactId>log4j</artifactId>
 <version>1.2.17</version>
 </dependency>
 * @author:gaoguangjin
 * @date:2018/5/25
 */
public class Log4JTest {
    private static final Logger log= Logger.getLogger(Log4JTest.class);

    public static void main(String[] args) {
        log.warn("a");
        log.debug("b");
        log.info("c");
        log.error("e");
    }
}
