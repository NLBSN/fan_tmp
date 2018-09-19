package com.vdata.kafka.pool;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @description kafka二次封装的生产者连接点
 * @author tzc
 *
 */
public class KafkaProducerApp<T, D> {
	//定义一个真正的kafka生产者
	private KafkaProducer<T, D> producer=null;
	
	public KafkaProducerApp(Properties properties){
		this.producer=new KafkaProducer<T, D>(properties);
		
	}
	
	//组建value方式的消息
	private ProducerRecord<T, D> toMessage(String topic,D value){
		return new ProducerRecord<T, D>(topic, value);
	}
	
	//组建key-value方式的消息
	private ProducerRecord<T, D> toMessage(String topic,T key,D value){
		return new ProducerRecord<T, D>(topic,key,value);
	}
	
	//组建key-value方式的消息发送到指定分区中
	private ProducerRecord<T, D> toMessage(String topic,T key,D value,int partition){
		return new ProducerRecord<T, D>(topic, partition, key, value);
	}
	
	/**
	 * @description 根据topic和value值进行写入数据到kafka中
	 * @param topic topic主题名称
	 * @param value	所需要写入的消息
	 * @return 返回一个写入后的状态信息
	 */
	public Future<RecordMetadata> send(String topic,D value){
		return this.producer.send(toMessage(topic, value));
	}
	
	/**
	 * @description 根据topic写入key-value的值到kafka中
	 * @param topic topic主题的名称
	 * @param key key值
	 * @param value value值
	 * @return 返回一个写入完成后的状态信息
	 */
	public Future<RecordMetadata> send(String topic,T key,D value){
		return this.producer.send(toMessage(topic,key,value));
	}
	
	/**
	 * @description 根据topic写入value的值到kafka指定的分区中
	 * @param topic topic主题的名称
	 * @param partition 分区
	 * @param value value值
	 * @return 返回一个写入完成后的状态信息
	 */
	public Future<RecordMetadata> send(String topic,D value,int partition){
		return this.producer.send(toMessage(topic,null,value,partition));
	}
	
	/**
	 * @description 根据topic写入key-value的值到kafka指定的分区中
	 * @param topic topic主题的名称
	 * @param key key值
	 * @param value value值
	 * @param partition 分区
	 * @return 返回一个写入完成后的状态信息
	 */
	public Future<RecordMetadata> send(String topic,T key,D value,int partition){
		return this.producer.send(toMessage(topic,null,value,partition));
	}
}
