package com.ggj.java.java.firstdemo.jiqun.partion;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

/**测试partion
 * 1、单个消息，单台服务器 partion数量1
 * 2、单个消息，单台服务器  partion 数量2
 * 3、单个消息，多台服务器
 *
 * @author:gaoguangjin
 * @date 2016/6/17 10:39
 */
public class ConsumerOne {

    static String TOPIC="topic1";
    static String GROUP_ID="cg9";
    public static void main(String[] args) {
        test(getKafkaConsumer());

    }

    private static void test(KafkaConsumer<String, String> kafkaConsumer) {
        kafkaConsumer.subscribe(Arrays.asList(TOPIC));
        while(true) {
            long lastOffset = 0;
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : consumerRecords) {
                System.out.printf("\n\r topic = %s ,partition = %d,offset = %d, key = %s, value = %s", record.topic(),record.partition(), record.offset(), record.key(), record.value());
                lastOffset = record.offset();
            }
            process();
            System.out.println("lastOffset read: " + lastOffset);
        }
    }


    public static KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092,123.56.118.135:9093,123.56.118.135:9094");
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9093,123.56.118.135:9094");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //group.id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        //value.deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        //auto.offset.reset从头开始取数据
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }

    private static void process()  {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
