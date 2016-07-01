package com.ggj.java.java.firstdemo.jiqun.partion;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 *   为了更好的实现负载均衡和消息的顺序性，kafka的producer在分发消息时可以通过分发策略发送给指定的partition。
 *   实现分发的程序是需要制定消息的key值，而kafka通过key进行策略分发。
 * @author:gaoguangjin
 * @date 2016/6/17 18:08
 */
public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int partition = 0;
        return partition;
    }
    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
