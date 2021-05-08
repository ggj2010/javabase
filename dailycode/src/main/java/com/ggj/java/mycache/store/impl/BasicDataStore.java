package com.ggj.java.mycache.store.impl;


import com.ggj.java.mycache.store.DataStore;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaoguangjin
 */
public class BasicDataStore<K,V> implements DataStore<K,V> {
    private ConcurrentHashMap<K,V> concurrentHashMap=new ConcurrentHashMap<K,V>();

    @Override
    public void put(K key, V value) {
        concurrentHashMap.put(key,value);
    }

    @Override
    public boolean remove(K key) {
        concurrentHashMap.remove(key);
        return false;
    }

    @Override
    public V get(K key) {
        return concurrentHashMap.get(key);
    }

    @Override
    public void clear() {
        concurrentHashMap.clear();
    }
}
