package com.ggj.java.tree;

import lombok.extern.slf4j.Slf4j;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash
 * <p>
 * 使用 TreeMap 数据结构，优点是该数据结构是有序的，无需再排序，
 * 而且该数据结构中有个函数叫 tailMap，作用是获取比指定的 key 大的数据集合
 *
 * @author gaoguangjin
 */
@Slf4j
public class ConsistentHashWithVritualNode {
    /**
     * 服务器列表
     */
    private static String[] servers = {"10.111.1.1:80", "10.111.1.2:80", "10.111.1.3:80"};
    /**
     * 服务器环
     */
    private static SortedMap<Integer, String> sortedMap = new TreeMap<Integer, String>();

    private static int vriatulNodeNum = 5;

    static {
        for (String ip : servers) {
            int hash = hash(ip);
            sortedMap.put(hash, ip);
            log.info("ip:{},hash:{}", ip, hash);
            for (int i = 1; i <= vriatulNodeNum; i++) {
                String vritualIp = ip + "v[" + i + "]";
                hash = hash(vritualIp);
                sortedMap.put(hash, ip);
                log.info("虚拟节点：vritualIp:{},hash:{}", vritualIp, hash);
            }
        }
    }

    /**
     * 使用的是 FNV1_32_HASH
     */
    public static int hash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) hash = Math.abs(hash);
        return hash;
    }

    /**
     * 获取顺时针离node 最近的点
     *
     * @param key
     * @return
     */
    public static String getNode(String key) {
        int hash = hash(key);

        //获取比指定的 key 大的数据集合
        SortedMap<Integer, String> resultMap = sortedMap.tailMap(hash);
        if (resultMap.isEmpty()) {
            //如果没有比该key的hash值大的，则从第一个node开始
            Integer i = sortedMap.firstKey();
            log.info("key:{} hash:{}  命中node:{}", key, hash, sortedMap.get(i));
            //返回对应的服务器
            return sortedMap.get(i);
        } else {
            //第一个Key就是顺时针过去离node最近的那个结点
            int firstHash = resultMap.firstKey();
            log.info("key:{} hash:{}  命中node:{}", key, hash, sortedMap.get(firstHash));
            return sortedMap.get(firstHash);
        }
    }

    public static void main(String[] args) {
        String node = getNode("114.250.25.147");
        String node2 = getNode("101.230.253.26");
        String node3 = getNode("108.230.253.26");
        String node4 = getNode("119.230.253.26");
    }

}
