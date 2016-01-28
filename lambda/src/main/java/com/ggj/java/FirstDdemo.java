package com.ggj.java;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/27 17:20
 */
@Slf4j
public class FirstDdemo {

//    (params) -> expression
//    (params) -> statement
//    (params) -> { statements }
    public static void main(String[] args) {
        list();
        thread();
        getMax();
    }

    private static void getMax() {
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x)
                .summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());

    }

    private static void thread() {
        new Thread(() -> log.info("In Java8, Lambda expression rocks !!")).start();
        new Thread(() -> log.info("test")).start();
    }

    private static void list() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("3");
        list.add("2");

        list.forEach(number -> log.info(number));

        list.forEach((String number) -> {log.info(number);});
    }
}
