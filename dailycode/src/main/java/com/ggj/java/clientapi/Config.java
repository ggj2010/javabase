package com.ggj.java.clientapi;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 假如有这样一个业务场景，从数据库里面查询某个key的值，并将查询到的值缓存到本地。
 * 如果并发的去查询某个key的值，怎么保证只会调用一次数据库。
 *
 * @author:gaoguangjin
 * @date:2017/10/25
 */
@Slf4j
public class Config {
    private ConcurrentMap<String, ConfigFuture> configFutures = new ConcurrentHashMap<String, ConfigFuture>();
    private ConcurrentMap<String, ConfigValue> cacheConfigValue = new ConcurrentHashMap<String, ConfigValue>();
    private Object object = new Object();

    private ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        int maxThreadSize = 10;
        Config config = new Config();
        ExecutorService executors = Executors.newFixedThreadPool(maxThreadSize);
        for (int i = 0; i < maxThreadSize; i++) {
            executors.execute(new Runnable() {
                @Override
                public void run() {
//                    log.info("get:{}", config.get("test", ""));
//                    config.getWithLock("test", "");
//                    config.getWithLockTwo("test", "");
                    config.getWithLockThree("test", "");
                }
            });
        }
        executors.shutdown();
        executors.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        long endTime = System.currentTimeMillis();
        log.info("耗时，{}ms", (endTime - beginTime));
    }

    /**
     * 18:37:30.128 [main] INFO  com.ggj.java.clientapi.Config - 耗时，16ms
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected String get(String key, String defaultValue) {
        ConfigValue configValue = readCache(key);
        if (configValue == null) {
            ConfigFuture existingFuture = null;
            //无锁版本1
            ConfigFuture future = new ConfigFuture(key);
            existingFuture = configFutures.putIfAbsent(key, future);
            if (existingFuture != null) {
                log.info("wait future");
                // wait for other threads to load the config
                try {
                    configValue = existingFuture.get();
                } catch (Exception e) {
                }
            } else {
                // load the config and notify other threads
                try {
                    configValue = get(key);
                    future.setResult(configValue);
                    writeCache(key, configValue);
                } catch (Exception e) {
                    future.setResult(e);
                } finally {
                    configFutures.remove(key);
                }
            }


            //无锁版本2 多次穿透数据库,多线程可能会生成多个，new ConfigFuture
            /*ConfigFuture future2 = configFutures.get(key);
            if(future2==null){
                future2 = configFutures.get(key);
                if(future2==null){
                    log.info("future2 is null");
                    configFutures.put(key, new ConfigFuture(key));
                    configValue = get(key);
                    writeCache(key, configValue);
                }
                //dd
            }else{
                try {
                    configValue = future2.get();
                } catch (Exception e) {
               }
            }*/
        }
        return configValue.getValue() == null ? defaultValue : configValue.getValue();
    }

    /**
     * 18:36:44.685 [main] INFO  com.ggj.java.clientapi.Config - 耗时，8ms
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getWithLock(String key, String defaultValue) {
        ConfigValue configValue = readCache(key);
        if (configValue == null) {
            synchronized (object) {
                configValue = readCache(key);
                if (configValue == null) {
                    configValue = get(key);
                    writeCache(key, configValue);
                }
            }
        }
        return configValue.getValue() == null ? defaultValue : configValue.getValue();
    }

    /**
     * 19:19:41.451 [main] INFO  com.ggj.java.clientapi.Config - 耗时，10ms
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getWithLockTwo(String key, String defaultValue) {
        ConfigValue configValue = readCache(key);
        if (configValue == null) {
            try {
                if (lock.tryLock(100, TimeUnit.SECONDS)) {
                    configValue = readCache(key);
                    if (configValue == null) {
                        configValue = get(key);
                        writeCache(key, configValue);
                    }
                }
            } catch (Exception e) {
                //
            } finally {
                lock.unlock();
            }
        }
        return configValue.getValue() == null ? defaultValue : configValue.getValue();
    }

    /**
     * 19:20:30.630 [main] INFO  com.ggj.java.clientapi.Config - 耗时，8ms
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getWithLockThree(String key, String defaultValue) {
        ConfigValue configValue = readCache(key);
        if (configValue == null) {
            try {
                lock.lock();
                configValue = readCache(key);
                if (configValue == null) {
                    configValue = get(key);
                    writeCache(key, configValue);
                }
            } catch (Exception e) {
                //
            } finally {
                lock.unlock();
            }
        }
        return configValue.getValue() == null ? defaultValue : configValue.getValue();
    }

    private ConfigValue get(String key) {
        log.info("----load key value from database----");
        return new ConfigValue("value");
    }

    private void writeCache(String key, ConfigValue configValue) {
        cacheConfigValue.putIfAbsent(key, configValue);
    }

    private ConfigValue readCache(String key) {
        return cacheConfigValue.get(key);
    }


}
