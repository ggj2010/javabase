package com.ggj.java;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

/**
 * @ClassName:RedisEasyTest.java
 * @Description: redis性能测试。10万条数据
 * @see:1、用原生态的jedis 最慢！！！！！！
 * @see:2、用事物transaction 33
 * @see:3、用pipelined 9秒
 * @see:4、用pipelined 里面开启事物 效率和 transaction一样
 * @author gaoguangjin
 * @Date 2015-5-27 下午4:33:21
 */
@Slf4j
public class RedisEasyTest {
    private static Jedis jedis = new Jedis("123.56.118.135");
    private static Jedis jedis2 = new Jedis("123.56.118.135");
    private static Jedis jedis3 = new Jedis("123.56.118.135");
    private static Pipeline p = jedis.pipelined();

//    private static int KEY_COUNT = 150000;
    private static int KEY_COUNT = 50000;

    public static void main(String[] args) {
        jedis.auth("gaoguangjin");
        jedis.select(1);
        jedis.flushDB();

        jedis2.auth("gaoguangjin");
        jedis2.select(1);
        Transaction transaction = jedis2.multi();
        jedis2.getClient().setConnectionTimeout(100000);


        jedis3.auth("gaoguangjin");
        jedis3.select(1);
        Pipeline pipeline = jedis3.pipelined();
        jedis3.getClient().setConnectionTimeout(100000);
        pipeline.multi();

        // 不要把注释打开，否则慢的要死！！！
        long start = System.currentTimeMillis();
       /*  jedis();
         System.out.printf("jedis use %d sec \n", (System.currentTimeMillis() - start) / 1000);*/
        start = System.currentTimeMillis();
        transation(transaction);
        System.out.printf("transation use %d sec \n", (System.currentTimeMillis() - start) / 1000);

        start = System.currentTimeMillis();
        piple();
        System.out.printf("batch piple use %d sec \n", (System.currentTimeMillis() - start) / 1000);

        // start = System.currentTimeMillis();
        // pipleWithTransation(pipeline);
        // System.out.printf("batch piple transation use %d sec \n", (System.currentTimeMillis() - start) / 1000);

    }

    private static void pipleWithTransation(Pipeline pipeline) {
        for (int i = 0; i < KEY_COUNT; i++) {
            pipeline.set("pipletransation" + i, i + "");
        }
        pipeline.exec();
    }

    private static void piple() {
        for (int i = 0; i < KEY_COUNT; i++) {
            p.set("piple" + i, i + "");
        }
        p.sync();
    }

    private static void transation(Transaction transaction) {
        for (int i = 0; i < KEY_COUNT; i++) {
            transaction.set("transaction" + i, i + "");
        }
        transaction.exec();

    }

    private static void jedis() {
        for (int i = 0; i < KEY_COUNT; i++) {
            jedis.set("normal" + i, i + "");
        }

    }

}
