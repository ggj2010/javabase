package com.ggj.java;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.UUID;

import static com.sun.javafx.css.SizeUnits.EX;

/**
 * @author:gaoguangjin
 * @date 2016/12/13 22:34
 */
@Slf4j
public class RedisLock {
    static Jedis jedis;
    static  {
        jedis = new Jedis("123.56.118.135", 6379);
        jedis.auth("gaoguangjin");
    }
    /**
     *
     * set gao ggj ex 30 nx
     EX   seconds  -- Set the specified expire time, in seconds.
     PX   milliseconds  -- Set the specified expire time, in milliseconds.
     NX  -- Only set the key if it does not already exist.
     XX  -- Only set the key if it already exist.
     * @param args
     */
    public static void main(String[] args) {
        String lockKey="lock_db";
        //防止误删锁
        String value= UUID.randomUUID().toString();
        //如果不存在 等于拿到锁返回ok值 ，如果存在了就返回null
        String result = jedis.set(lockKey, value, "NX", "EX", 60);
        log.info("value"+result);

        if(result!=null&&"ok".equals(result)){
            //拿到锁 处理其他逻辑
            String lockValue=jedis.get(lockKey);
            //举个例子，一个客户端拿到了锁，被某个操作阻塞了很长时间，过了超时时间后自动释放了这个锁，然后这个客户端之后又尝试删除这个其实已经被其他客户端拿到的锁。
            // 所以单纯的用DEL指令有可能造成一个客户端删除了其他客户端的锁，
            // 用上面这个脚本可以保证每个客户单都用一个随机字符串’签名’了，这样每个锁就只能被获得锁的客户端删除了。
            if(lockValue!=null&&value.equals(lockValue)){

            }
            //
        }

    }
}
