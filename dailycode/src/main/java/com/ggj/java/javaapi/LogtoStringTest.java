
package com.ggj.java.javaapi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date:2018/6/20
 */
@Slf4j
public class LogtoStringTest {
    public static void main(String[] args) {
        String a = Long.toString(1, 32);
        String b = Long.toString(12, 32);
        String c = Long.toString(12345678900L, 32);
        log.info(a);
        log.info(b);
        log.info(c);


        System.out.println( 1&15 );
        System.out.println( 2&15 );
        System.out.println( 12345&15 );
    }
}
