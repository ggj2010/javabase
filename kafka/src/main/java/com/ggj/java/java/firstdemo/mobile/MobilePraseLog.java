package com.ggj.java.java.firstdemo.mobile;

import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;

/**
 * 测试手机端那个项目 用来接收log
 * @author:gaoguangjin
 * @date 2016/5/24 16:09
 */
public class MobilePraseLog extends ShutdownableThread {
    KafkaConsumer<String, String> consumer;

    private static final String TOPIC ="requestlog" ;

    public static void main(String[] args) {
        MobilePraseLog consumerThread = new MobilePraseLog("consumerClient",false);
        consumerThread.start();
    }

    public MobilePraseLog(String name, boolean isInterruptible) {
        super(name, isInterruptible);
        consumer=getKafkaConsumer();
    }

    @Override
    public void doWork() {
        consumer.subscribe(Collections.singletonList(TOPIC));
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
            //// TODO: 2016/5/24  可以将log加工处理
            System.out.println("得到log:"+record.value());
        }
    }

    public static KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //group.id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "loggroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        //value.deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class.getName());
        return new KafkaConsumer<>(props);
    }

}
