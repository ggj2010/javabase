package com.ggj.java;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * log4j2
 * <dependency>
 <groupId>org.apache.logging.log4j</groupId>
 <artifactId>log4j-api</artifactId>
 <version>2.5</version>
 </dependency>
 <dependency>
 <groupId>org.apache.logging.log4j</groupId>
 <artifactId>log4j-core</artifactId>
 <version>2.5</version>
 </dependency>
 * @author:gaoguangjin
 * @date:2018/5/25
 */
public class Log4j2 {
    private static final Logger log= LogManager.getLogger(Log4j2.class);

    public static void main(String[] args) {
        log.warn("a");
        log.debug("b");
        log.info("c");
        log.error("e");
    }


}
