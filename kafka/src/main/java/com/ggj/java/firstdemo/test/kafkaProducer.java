package com.ggj.java.firstdemo.test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;



/**
 * ��������
 * ������: 0
������: 1
������: 2
������: 3
������: 4
������: 5
������: 6
������: 7
������: 8
������: 9
������: 10
������: 11
������: 12
������: 13
������: 14
������: 15
������: 16
������: 17
������: 18
 * @author zm
 *
 */
public class kafkaProducer extends Thread{

	private String topic;
	
	public kafkaProducer(String topic){
		super();
		this.topic = topic;
	}
	
	
	@Override
	public void run() {
		Producer producer = createProducer();
		int i=0;
		while(true){
			producer.send(new KeyedMessage<Integer, String>(topic, "message: " + i));
			System.out.println("������: " + i);
			try {
				TimeUnit.SECONDS.sleep(1);
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Producer createProducer() {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", "123.56.118.135:2181");//����zk
		properties.put("serializer.class", StringEncoder.class.getName());
		properties.put("metadata.broker.list", "123.56.118.135:9092");// ����kafka broker
		return new Producer<Integer, String>(new ProducerConfig(properties));
	 }
	
	
	public static void main(String[] args) {
		new kafkaProducer("test").start();// ʹ��kafka��Ⱥ�д����õ����� test 
		
	}
	 
}
