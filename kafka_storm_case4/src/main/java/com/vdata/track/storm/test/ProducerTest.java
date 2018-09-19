package com.vdata.track.storm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.producer.Producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

/**
 * @description kafka生产者测试
 * @author tzc
 *
 */
public class ProducerTest {

	@Test
	public void test() throws Exception {
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
		
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		
		InputStream in=new FileInputStream(new File("testdata"));
		InputStreamReader reader=new InputStreamReader(in);
		BufferedReader buffer=new BufferedReader(reader);
		List<String> list=new ArrayList<String>();
		while(buffer.ready()){
			String line=buffer.readLine();
			list.add(line);
		}
		buffer.close();
		
		int j=0;
		while(j<100){
			for (int i = 0; i < list.size(); i++){
				producer.send(new ProducerRecord<String, String>("vdata",
						Integer.toString(i),list.get(i)));
				j++;
			}
		}
		producer.close();
	}
}
