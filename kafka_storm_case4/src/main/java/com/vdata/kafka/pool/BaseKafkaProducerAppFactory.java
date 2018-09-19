package com.vdata.kafka.pool;

import java.util.Properties;

/**
 * @decription 生产者工厂类
 * @author tzc
 *
 * @param <T> key值的类型
 * @param <D> value值的类型
 */
public class BaseKafkaProducerAppFactory<T, D> extends KafkaProducerAppFactory<T, D> {
	//生产者连接所需要的属性
	private Properties properties=null;
	
	public BaseKafkaProducerAppFactory(Properties properties) {
		this.properties=properties;
	}
	
	@Override
	public KafkaProducerApp<T, D> newInstance() {
		return new KafkaProducerApp<T, D>(properties);
	}

}
