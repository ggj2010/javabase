package com.ggj.java.java.firstdemo.jiqun.partion;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * 1、单个消息，单台服务器 partion数量1
 * 2、单个消息，单台服务器  partion 数量2
 * 3、单个消息，多台服务器
 */
public class ProducerOne {
    //partion 每个broker 都有设置partion的数量
    static Integer PARTION=0;
//    static Integer PARTION=1;
    static String TOPIC="topic1";
    public static void main(String[] args) {
        test(getProducer());
    }

    private static void test(KafkaProducer producer) {
        int i=0;
        while(true){
             ProducerRecord record = new ProducerRecord<String, String>(TOPIC,PARTION, i+"", "单个消息"+i);
             producer.send(record);
             System.out.println("send message:"+i);
            i++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 构造KafkaProducer
     * @return
     */
    private static KafkaProducer<Integer, String> getProducer() {
        Properties properties = new Properties();
        //集群
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092,123.56.118.135:9093,123.56.118.135:9094");
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9093,123.56.118.135:9094");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //key 和 value serializer的类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //partion key 规则 默认的是org.apache.kafka.clients.producer.internals.DefaultPartitioner
//        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class.getName());
        return new KafkaProducer(properties);
    }


}
