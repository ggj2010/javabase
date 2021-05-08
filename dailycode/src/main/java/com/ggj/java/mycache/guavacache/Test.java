package com.ggj.java.mycache.guavacache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 把数据实体自动载入到缓存中去-》基本特性
 * 当缓存到达最大数量时回收最少使用的数据-》限制最大内存，避免内存被占满-》高级特性
 * 基于时间的实体有效期，依据最后访问或写时间-》基本特性，但很细腻
 * 缓存的keys自动用，弱引用封装-》利于GC回收-》
 * 回收或被移除实体可收到通知-》
 * 缓存的访问统计-》
 * @author gaoguangjin
 */
@Slf4j
public class Test {


    public static void main(String[] args) {
        normal();
        //初始化就指定数据源加载
        loadingCache();
        //并发获取缓存
        testConcurrent();
        //失效策略
        testexpire();
    }

    /**
     * 搭配loading cache使用
     */
    private static void testexpire() {
        LoadingCache loadingCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                //固定失效时间
                .expireAfterWrite(2, TimeUnit.SECONDS)
                //指创建或写之后的固定有效期到达时，数据会被自动从缓存中移除。读写操作都会重置访问时间
                //.expireAfterAccess(2, TimeUnit.SECONDS);
                //指创建或写之后的固定有效期到达时,数据自动刷新
                //.refreshAfterWrite(2, TimeUnit.SECONDS);
                //.removalListener(MY_LISTENER)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return createCache(key);
                    }
                });
        try {

            Thread.sleep(2000);
            System.out.println(loadingCache.get("a"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 如果并发换取缓存，缓存不存在的话，更新的只会有一个线程获取到更新权限
     */
    private static void testConcurrent() {
        Cache<String, String> cache = CacheBuilder.newBuilder().build();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {

                Object value2 = null;
                try {
                    value2 = cache.get("a", new Callable() {
                        @Override
                        public Object call() throws Exception {
                            log.info("get  key={} data from db", "a");
                            return "testConcurrent";
                        }
                    });
                } catch (ExecutionException e) {
                }
                System.out.println(value2);
            }).start();
        }
    }

    /**
     * LoadingCache是Cache的子接口，相比较于Cache，
     * 当从LoadingCache中读取一个指定key的记录时，如果该记录不存在，则LoadingCache可以自动执行加载数据到缓存的操作
     */
    private static void loadingCache() {
        LoadingCache loadingCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                //.removalListener(MY_LISTENER)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return createCache(key);
                    }
                });
        try {
            Object value = loadingCache.get("a");
            log.info(value.toString());
        } catch (ExecutionException e) {
            log.error("get cache error", e);
        }

    }

    private static void normal() {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
        Cache cache = cacheBuilder.build();
        cache.put("a", "11111");

        Object value = cache.getIfPresent("a");
        System.out.println(value);

        //如果缓存没有 会调用Callable 获取到值之后会放到缓存里面
        String key = "b";
        try {
            Object value2 = cache.get(key, new Callable() {
                @Override
                public Object call() throws Exception {
                    log.info("get  key={} data from db", key);
                    return "2222";
                }
            });
            System.out.println("b:==>"+value2);
            Object value3 = cache.getIfPresent("c");
            System.out.println("c==>:"+value3);
        } catch (ExecutionException e) {
            log.error("get data from cache error", e);
        }
    }

    private static String createCache(String key) {
        try {
            Thread.sleep(1000);
            log.info("get  key={} data from db", key);
            return key + ":value";
        } catch (Exception e) {
        }
        return "";
    }
}
