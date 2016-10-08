package com.ggj.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author:gaoguangjin
 * @date 2016/10/8 16:01
 */
@Slf4j
public class RedisTestHmgetAndHget {
    private static Jedis jedis = new Jedis("123.56.118.135");
    private static int size = 10000;

    public static void main(String[] args) {
        jedis.auth("gaoguangjin");
        jedis.select(2);
        jedis.flushDB();
        List<String> list = new ArrayList<>(size);
        String[] array = new String[size];
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(i + "", i + "");
            list.add(i + "");
        }
        jedis.hmset("set", map);
        list.toArray(array);

        long beginTime = System.currentTimeMillis();
        beginTime = System.currentTimeMillis();
        jedis.hmget("set", array);
        log.info("hmget 耗时{}", System.currentTimeMillis() - beginTime);

        for (int i = 0; i <= size; i++) {
            jedis.hget("set", i + "");
        }

        log.info("hget 耗时{}", System.currentTimeMillis() - beginTime);
    }
}
