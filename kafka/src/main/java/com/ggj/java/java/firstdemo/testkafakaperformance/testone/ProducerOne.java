package com.ggj.java.java.firstdemo.testkafakaperformance.testone;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;

/**
 * 发送10w数据
 * @author:gaoguangjin
 * @date 2016/6/17 10:40
 */
public class ProducerOne {
    //10w条数据
    static int testNumber=100000;
    static String TOPIC_TESTONE_PERFORMANCE="ggj";
    //partion
    static Integer PARTION=0;
    public static void main(String[] args) throws IOException {
        long beginTime=System.currentTimeMillis();
        test(getProducer());
        long endTime=System.currentTimeMillis();
        System.out.println(testNumber+"数据，耗时："+(endTime-beginTime)/1000+"秒");
        System.in.read();
    }

    private static void test(KafkaProducer<String, String>  producer) {
        for (int i =testNumber; i >0 ; i--) {
            //partion 随机
            ProducerRecord record = new ProducerRecord<String, String>(TOPIC_TESTONE_PERFORMANCE, i%2, i+"", i+"");
            producer.send(record);
        }
    }

    /**
     * 构造KafkaProducer
     * @return
     */
    private static KafkaProducer<String, String> getProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //key 和 value serializer的类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(properties);
    }
}

