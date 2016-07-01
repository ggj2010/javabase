package com.ggj.java.java.firstdemo.testkafakaperformance.testone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author:gaoguangjin
 * @date 2016/6/17 10:39
 */
public class ConsumerOne {
    //10w条数据
    static int testNumber=100000;
    static String TOPIC_TESTONE_PERFORMANCE="ggj";
    public static void main(String[] args) {
        long beginTime=System.currentTimeMillis();
        test(getKafkaConsumer());
        long endTime=System.currentTimeMillis();

        System.out.println("消费："+testNumber+"数据，耗时："+(endTime-beginTime)/1000+"秒");
    }

    private static void test(KafkaConsumer<String, String> kafkaConsumer) {
        kafkaConsumer.subscribe(Arrays.asList(TOPIC_TESTONE_PERFORMANCE));
        for(int i=0;i<=testNumber;i++){
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : consumerRecords) {
                System.out.printf("\n\roffset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
            }
        }

    }


    public static KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //group.id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testone");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        //value.deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}
