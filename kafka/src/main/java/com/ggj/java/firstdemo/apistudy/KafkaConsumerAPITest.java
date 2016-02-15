package com.ggj.java.firstdemo.apistudy;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/6 15:48
 */
public class KafkaConsumerAPITest {


    public static void main(String[] args) {
        KafkaConsumer<Integer, String> consumer = getKafkaConsumer();

      // listTopics(consumer);

        subscribe(consumer);
    }

    /**
     * 订阅topic
     * @param consumer
     */
    private static void subscribe(KafkaConsumer<Integer, String> consumer) {
        consumer.subscribe(Arrays.asList(KafKaProducerAPITest.TOPIC_API));
        while (true) {
            System.out.println("==start get value beign===");
            ConsumerRecords<Integer, String> records = consumer.poll(100);
            System.out.println("==start get value end===");
            for (ConsumerRecord<Integer, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
        }
    }

    /**
     * list topic test
     * @param consumer
     */
    private static void listTopics(KafkaConsumer<Integer, String> consumer) {
        Map<String, List<PartitionInfo>> partitionInfo = consumer.listTopics();
        partitionInfo.forEach((String key,List<PartitionInfo> list)->{
            System.out.println("key = [" + key + "]");

            list.forEach((PartitionInfo partition)->{
               // partition.topic();
                System.out.println("PartitionInfo = [" + partition + "]");
            });
        });
    }

    public static KafkaConsumer<Integer, String> getKafkaConsumer() {
        //配置咯
        Properties props = new Properties();
        //服务器开启两个server 一个端口号是9092 一个是9093
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092,123.56.118.135:9093");
        //bootstrap.servers
        //props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //group.id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ggj");
        //enable.auto.commit
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        //auto.commit.interval.ms
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "999999999999");

        //max.partition.fetch.bytes 最大字节提交大小
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1024");
        //max.partition.fetch.bytes
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "1000");
        //session.timeout.ms
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        //heartbeat.interval.ms 心跳时间
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000");
        //key.deserialize
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        //value.deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        return new KafkaConsumer<>(props);
    }


}
