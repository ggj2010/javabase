package com.ggj.java;

import org.apache.log4j.Logger;

/**
 * 在pom里面引入
 *
 *
 *
 *  <dependency>
 <groupId>org.slf4j</groupId>
 <artifactId>log4j-over-slf4j</artifactId>
 <version>${slf4j.version}</version>
 </dependency>
 同时去除
 <dependency>
 <groupId>log4j</groupId>
 <artifactId>log4j</artifactId>
 <version>1.2.17</version>
 </dependency>

 * log4j转换成sl4j的logback 形式
 * @author:gaoguangjin
 * @date:2018/5/25
 */
public class Log4jToSl4jTest {

    private static final Logger log= Logger.getLogger(Log4jToSl4jTest.class);

    public static void main(String[] args) {
        log.warn("a");
        log.debug("b");
        log.info("c");
        log.error("e");
    }
}
