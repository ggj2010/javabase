package com.ggj.java;

/**
 * zsort 实现排序相同的时候按照时间排序
 * 可以将zsort存储的数据按照高32存数据，低32位置存时间
 *
 * @author gaoguangjin
 */
public class BitTest {
    private static final Long COUNT_BITS = 32L;

    public static void main(String[] args) throws InterruptedException {
        Long time=System.currentTimeMillis()/1000L;
        Thread.sleep(1000);
        Long time2=System.currentTimeMillis()/1000L;
        Long a=100L;
        Long b=2L;
        System.out.println(ctlOf(a<<COUNT_BITS,time));
        System.out.println(ctlOf(a<<COUNT_BITS,time2));


        System.out.println(ctlOf(b<<COUNT_BITS,time));
        System.out.println(ctlOf(b<<COUNT_BITS,time2));


    }

    private static Long ctlOf(Long id, Long time) {
        return id | time;
    }
}
