package com.ggj.java.firstdemo.myself;

import kafka.server.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
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
 * Date 2016/1/29 18:39
 */

@Slf4j
public class KafkaProducerTest {
    private KafkaProducer<Integer, String> producer;

    public KafkaProducerTest() {
        producer = getProducer();
    }


    public static void main(String[] args) {
        new KafkaProducerTest().sendMessage();
    }

    /**
     * 构造KafkaProducer
     *
     * @return KafkaProducer
     */
    private KafkaProducer<Integer, String> getProducer() {
        Properties properties = new Properties();
        //bootstrap.servers
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "123.56.118.135:9092");
        //client.id
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerTest");
        //key 和 value serializer的类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(properties);
    }

    public void sendMessage() {
        new Thread(() -> {
            int number = 1;
            long startTime = System.currentTimeMillis();
            while (true) {
                String messageStr = number + ": <==>";
                ProducerRecord record = new ProducerRecord<Integer, String>(KafkaConsumerTest.TOPIC_ONE, number, messageStr);
                //ProducerCallBack 为发送完回调的
                producer.send(record, new ProducerCallBack(startTime, number, messageStr));
                try {
                    // Send synchronously 发送的时候加锁，等待返回结果
//                    producer.send(record,new ProducerCallBack(startTime, number, messageStr)).get();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.error("休眠失败" + e.getLocalizedMessage());
                }
                number++;
            }
        }).start();

    }


}
