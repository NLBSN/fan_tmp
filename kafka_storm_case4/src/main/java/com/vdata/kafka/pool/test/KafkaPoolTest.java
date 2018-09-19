package com.vdata.kafka.pool.test;

import java.util.Properties;

import org.junit.Test;

import com.vdata.kafka.pool.KafkaPool;
import com.vdata.kafka.pool.KafkaProducerApp;

public class KafkaPoolTest {

	@Test
	public void producerTest() throws Exception{
		Properties props = new Properties();
		props.put("bootstrap.servers", "vdata1:9092,vdata2:9092,vdata3:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		
		KafkaPool<String, String> pool=new KafkaPool<String, String>(props);
		for(int i=0;i<1000;i++){
			KafkaProducerApp<String, String> producer=pool.borrowProducer();
			producer.send("vdata", "test"+i);
			pool.returnProducer(producer);
			System.out.println("-------------"+i);
		}
	}
}
