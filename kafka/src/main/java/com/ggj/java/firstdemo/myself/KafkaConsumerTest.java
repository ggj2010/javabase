package com.ggj.java.firstdemo.myself;

import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * author:gaoguangjin
 * Description:最简单的消费者写法，
 * 可以在linux命令行 输入bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
 * Email:335424093@qq.com
 * Date 2016/1/29 13:22
 */

@Slf4j
public class KafkaConsumerTest extends ShutdownableThread {

    private static  KafkaConsumer<Integer, String>  consumer=null;
    public final static String TOPIC_ONE="test";
    public final static String TOPIC_TWO="test2";

    public KafkaConsumerTest() {
        super("KafkaConsumerExample", false);
        consumer=getKafkaConsumer();
    }

    public static void main(String[] args) {
        KafkaConsumerTest test=new KafkaConsumerTest();
        test.start();
    }


    public static KafkaConsumer<Integer, String> getKafkaConsumer() {
        //配置咯
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ggj");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

       return  new KafkaConsumer<>(props);
    }

    @Override
    public void doWork() {
        //可以同时订阅多个topic
        consumer.subscribe( Arrays.asList(TOPIC_ONE,TOPIC_TWO));
//        consumer.subscribe(Collections.singletonList(TOPIC_ONE));
        ConsumerRecords<Integer, String> records = consumer.poll(1000);
        for (ConsumerRecord<Integer, String> record : records) {
            System.out.println("消费者Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
        }
    }

}
