package com.ggj.java.mycache;

import com.ggj.java.mycache.store.DataStore;

/**
 * @author gaoguangjin
 */
public class CsCache<K, V> {

    private DataStore<K, V> dataStore;

    public CsCache(DataStore<K, V> dataStore) {
        this.dataStore = dataStore;
    }

    public V get(K k) {
        return dataStore.get(k);
    }

    public void put(K k, V v) {
        dataStore.put(k, v);
    }

    public boolean remove(K k) {
        return dataStore.remove(k);
    }

    public void clear() {
        dataStore.clear();
    }


}
