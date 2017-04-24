package com.ggj.java.lock;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * 秒杀库存lock
 *
 * @author:gaoguangjin
 * @date 2016/12/19 16:33
 */
@Slf4j
public class SeckillLock {

    public final static int threadSize = 10000;
    public final static String SECKILL_LOCK_KEY = "seckill_key_id";
    public final static String ITEM_NUMBER_KEY = "item_number";
    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMaxIdle(100);
        poolConfig.setMaxTotal(threadSize);
        poolConfig.setMinIdle(20);
        poolConfig.setMaxWaitMillis(5000);
        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 1000, "gaoguangjin");
    }

    //库存
    public static void main(String[] args) {
        initThread();
    }

    /**
     * 13:59:42.967 [main] INFO  com.ggj.java.lock.SeckillLock - 库存数量：0 耗时 4865ms
     * 13:59:42.967 [main] INFO  com.ggj.java.lock.SeckillLock - 2000 个/s
     */
    private static void initThread() {
        Jedis jedis = jedisPool.getResource();
        //商品库存50个
        jedis.set(ITEM_NUMBER_KEY, "10");
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            startThead(startSignal, countDownLatch);
        }
        try {
            startSignal.countDown();
            long beginTime = System.currentTimeMillis();
            countDownLatch.await();
            log.info("库存数量：{} 耗时 {}ms", jedis.get(ITEM_NUMBER_KEY), (System.currentTimeMillis() - beginTime));
            log.info("{} 个/s", threadSize / (System.currentTimeMillis() - beginTime) * 1000);
        } catch (Exception e) {
            log.error("e" + e);
        } finally {
            jedisPool.returnBrokenResource(jedis);
        }
    }


    public static void startThead(CountDownLatch startSignal, CountDownLatch countDownLatch) {
        new Thread() {
            @Override
            public void run() {
                try {
                    startSignal.await();
                    //使用锁
                    seckill(true);
                    //不用锁
//                    seckill(false);
                } catch (Exception e) {
                    log.error("e" + e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        }.start();
    }

    /**
     * 这种方式会导致 Could not get a resource from the pool
     * 因为连接没有及时释放，导致连接池不够用，并发时候要特别注意
     * @param flag
     */
    private static void seckill(boolean flag) {
        Jedis jedis = jedisPool.getResource();
        String lockValue = "";
        String value = UUID.randomUUID().toString();
        try {
            if(flag) {
                //1、是否得到锁
                while (!isGetLock(value, jedis)) {
                }
            }
            //2、先检查库存
            String number = jedis.get(ITEM_NUMBER_KEY);
            if (Integer.parseInt(number) > 0) {
                doTask(jedis, value);
            } else {
                log.info("{} 库存为零：秒杀失败", value);
            }
        } catch (BizException e) {
            log.info("秒杀失败:{}", e.getMessage());
        } catch (Exception e) {
            log.error("e" + e);
        } finally {
            //比较当前锁内容是否一致。
            lockValue = jedis.get(SECKILL_LOCK_KEY);
            if (lockValue != null && value.equals(lockValue)) {
                jedis.del(SECKILL_LOCK_KEY);
                log.info("{} 释放锁", value);
            }
            jedisPool.returnResource(jedis);
        }

    }


    private static boolean isGetLock(String value, Jedis jedis) throws InterruptedException, BizException {
        /*检查库存这段代码可以大大优化性能*/
        String number = jedis.get(ITEM_NUMBER_KEY);
        if (Integer.parseInt(number) <= 0)
            throw new BizException("库存不够，秒杀失败");
        /*检查库存这段代码可以大大优化性能*/
        //建设

        String result = jedis.set(SECKILL_LOCK_KEY, value, "NX", "EX", 10);
        if (result != null && "OK".equals(result)) {
            log.info("{} 得到锁", value);
            return true;
        }
        return false;
    }

    private static void doTask(Jedis jedis, String value) {
        jedis.decr(ITEM_NUMBER_KEY);
        log.info("{} 秒杀成功！", value);
    }


    static class BizException extends Exception {
        BizException(String message) {
            super(message);
        }
    }

}
