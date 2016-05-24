package com.ggj.encrypt.common.utils.kafka;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/5/24 14:58
 */
@Slf4j
@Component
public class KafkaClientUtil implements InitializingBean {

    @Value("${kafka.bootstrapServers}")
    private String servers;
    @Value("${kafka.topic.requestlog}")
    private String requestlog;

    private KafkaProducer kafkaProducer;

    /**
     * 构造KafkaProducer
     * @return
     */
    private  KafkaProducer<Integer, String> getProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        //key 和 value serializer的类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(properties);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        kafkaProducer=getProducer();
    }

    /**
     * 构造ProducerRecord
     * @param topic
     * @param partition
     * @param key
     * @param value
     * @return
     */
    private  ProducerRecord<String,String> getProducerRecord(String topic,Integer partition, String key,String value) {
        if(StringUtils.isEmpty(topic))topic=requestlog;
        return new ProducerRecord<String,String>(topic,partition,key,value);
    }


    public void send(String topic,Integer partition, String key,String value){
        kafkaProducer.send(getProducerRecord(topic,partition,key,value));
    }

    /**
     * 默认分区发送
     * @param value
     */
    public void send(String value){
        kafkaProducer.send(getProducerRecord("",0,"",value));
    }

}
