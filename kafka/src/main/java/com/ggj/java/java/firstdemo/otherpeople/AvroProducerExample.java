package com.ggj.java.java.firstdemo.otherpeople;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;

/**
 * 发送 Avro格式 信息
 * Create and publish an avro type message.
 */
public class AvroProducerExample {

    public static void main(String[] str) throws InterruptedException, IOException {

        System.out.println("Starting ProducerAvroExample ...");

        sendMessages();


    }

    private static void sendMessages() throws InterruptedException, IOException {

        Producer<String, byte[]> producer = createProducer();

        sendRecords(producer);
    }

    private static Producer<String, byte[]> createProducer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "123.56.118.135:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        return new KafkaProducer(props);
    }

    private static void sendRecords(Producer<String, byte[]> producer) throws IOException, InterruptedException {
        String topic = "avro-topic";
        int partition = 0;
        while (true) {
            for (int i = 1; i < 100; i++)
                producer.send(new ProducerRecord<String, byte[]>(topic, partition, Integer.toString(0), record(i + "")));

            Thread.sleep(1000);
        }
    }


    private static byte[] record(String name) throws IOException {

        GenericRecord record = new GenericData.Record(AvroSupport.getSchema());
        record.put("firstName", name+"ggj");
        return AvroSupport.dataToByteArray(AvroSupport.getSchema(), record);

    }


}
