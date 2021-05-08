package com.ggj.java.mycache;

import lombok.extern.slf4j.Slf4j;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

/**
 * @author gaoguangjin
 */
@Slf4j
public class TestJSR {
    public static void main(String[] args) {
        //获取指定当cache实现类
        CachingProvider cachingProvider = Caching.getCachingProvider();
        //获取CacheManager
        CacheManager cacheManager = cachingProvider.getCacheManager();
        //创建cache实例
        Cache<Object, Object> cache = cacheManager.createCache("student", null);
        //往cacahe放值
        cache.put("gao","gaoguangjin");
        log.info((String) cache.get("gao"));
    }
}
