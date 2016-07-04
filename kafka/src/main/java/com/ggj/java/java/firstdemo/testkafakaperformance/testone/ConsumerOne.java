package com.ggj.java.java.firstdemo.testkafakaperformance.testone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

import static com.ggj.java.java.firstdemo.testkafakaperformance.testone.ProducerOne.testNumber;


/**
 * @author:gaoguangjin
 * @date 2016/6/17 10:39
 */
public class ConsumerOne {
    public static void main(String[] args) {
        test(getKafkaConsumer());
    }
    //    //消费offset = 1000199, key = 1, value = 144919毫秒
    private static void test(KafkaConsumer<String, String> kafkaConsumer) {
        kafkaConsumer.subscribe(Arrays.asList(TopicOne.TOPIC));
        long beginTime=System.currentTimeMillis();
        while(true){
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : consumerRecords) {
                if("1".equals(record.key())) {
                    long endTime = System.currentTimeMillis();
                    System.out.printf("\n\roffset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
                    System.out.println((endTime - beginTime) + "毫秒");
                }
            }
        }
    }

    public static KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //group.id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testone4");
      //  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        //value.deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }
}
