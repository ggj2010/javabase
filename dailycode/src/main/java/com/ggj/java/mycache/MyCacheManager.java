package com.ggj.java.mycache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 它定义了建立，配置，得到，管理和控制0个或多个有着唯一名字的Cache ，
 * 一个CacheManager被包含在单一的CachingProvider.
 * @author gaoguangjin
 */
public class MyCacheManager implements CacheManager {
    private Map<String,Cache> cacheMap=new ConcurrentHashMap<>();
    @Override
    public CachingProvider getCachingProvider() {
        return null;
    }

    @Override
    public URI getURI() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName, C c) throws IllegalArgumentException {
        if(isClosed()){
            throw  new IllegalStateException();
        }
        //check 略
        if(cacheMap.containsKey(cacheName)){
           throw new IllegalArgumentException("cache:"+cacheName+" already exists.") ;
        }else{
            MyCache myCache=new MyCache(this,cacheName,c);
            cacheMap.putIfAbsent(cacheName,myCache);
            return myCache;
        }
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s, Class<K> aClass, Class<V> aClass1) {
        return null;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s) {
        if(cacheMap.containsKey(s)){
            return cacheMap.get(s);
        }
        return null;
    }

    @Override
    public Iterable<String> getCacheNames() {
        return null;
    }

    @Override
    public void destroyCache(String s) {

    }

    @Override
    public void enableManagement(String s, boolean b) {

    }

    @Override
    public void enableStatistics(String s, boolean b) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
