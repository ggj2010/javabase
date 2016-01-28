package com.ggj.java.firstdemo;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * author:gaoguangjin
 * Description:kafaka消费者
 * Email:335424093@qq.com
 * Date 2016/1/28 18:12
 */
@Slf4j
public class KafkaConsumer {
    public static void main(String[] args) {
        receive();
    }

    private static void receive() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "123.56.118.135:2181");//zookeeper
        properties.put("group.id", "group1");

        ConsumerConnector consumer= Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();

        topicCountMap.put(KafkaProducer.TOPIC, 1); // 一次从主题中获取一个数据
        Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);

        KafkaStream<byte[], byte[]> stream = messageStreams.get(KafkaProducer.TOPIC).get(0);// 获取每次接收到的这个数据
        ConsumerIterator<byte[], byte[]> iterator =  stream.iterator();
        while(iterator.hasNext()){
            String message = new String(iterator.next().message());
           log.info("接收到: " + message);
        }
    }
}
