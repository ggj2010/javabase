package com.ggj.java.firstdemo;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Properties;

/**
 * author:gaoguangjin
 * Description:kafka生产者
 * Email:335424093@qq.com
 * Date 2016/1/28 18:12
 */
@Slf4j
public class KafkaProducer {

    public static final String TOPIC = "test";

    /**
     * 第一个demo
     *
     * @param args
     */
    public static void main(String[] args) {
        demo();
    }

    private static void demo() {
        Properties properties = new Properties();
        //所有配置的端口号都是默认的
        properties.put("zookeeper.connect", "123.56.118.135:2181");//声明zk
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("metadata.broker.list", "123.56.118.135:9092");// 声明kafka broker
        Producer<Integer, String> producer = new Producer<Integer, String>(new ProducerConfig(properties));
        //发送消息

        producer.send(new KeyedMessage<Integer, String>(TOPIC, "这是第一条信息"));
    }

}
