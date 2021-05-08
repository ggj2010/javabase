package com.ggj.java.mycache.store;

/**
 * @author gaoguangjin
 */
public interface DataStore<K,V> {
   void put(K key,V value);
   boolean remove(K key);
   V get(K key);
   void clear();
}
