package com.ggj.java.firstdemo.apistudy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/15 9:46
 */
public class KafKaProducerAPITest {
    public final static String TOPIC_API = "topicapi";


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer<Integer, String> producer = getProducer();
        //构造producerRecord
        ProducerRecord producerRecord=getProducerRecord();

        //普通send
        while (true) {
            Thread.sleep(1000);
            producer.send(producerRecord);
        }
        //带callback的send
        //  producer.send(producerRecord,new CallBackAPI("send message"));

        //send发送时候加锁，一个一个发送
        //producer.send(producerRecord).get();

       // producer.close();
    }

    /**
     * ProducerRecord 提供多种构造方法  String topic, Integer partition, K key, V value  随意搭配
     *
     * @return
     */
    private static ProducerRecord getProducerRecord() {
        //ProducerRecord
        ProducerRecord producerRecord = new ProducerRecord(TOPIC_API, "ggj-2016-02-15");

//        ProducerRecord producerRecord2=new ProducerRecord(TOPIC_API,"key","value");
        //partition
//        ProducerRecord producerRecord3=new ProducerRecord(TOPIC_API,1,"key","value");
        return producerRecord;
    }


    /**
     * get kafkaProducer
     *
     * @return
     */
    private static KafkaProducer<Integer, String> getProducer() {
        Properties properties = new Properties();
        //bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092,123.56.118.135:9093");
        //client.id
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerTest");
        //key 和 value serializer的类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(properties);
    }

}
