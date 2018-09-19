package com.vdata.kafka.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @description 连接池的工厂类
 * @author tzc
 *
 */
public class PooledKafkaProducerAppFactory<T, D> extends BasePooledObjectFactory<KafkaProducerApp<T, D>> {
	private KafkaProducerAppFactory<T, D> factory=null;
	
	public PooledKafkaProducerAppFactory(KafkaProducerAppFactory<T, D> factory){
		this.factory=factory;
	}
	
	@Override
	public KafkaProducerApp<T, D> create() throws Exception {
		return this.factory.newInstance();
	}

	@Override
	public PooledObject<KafkaProducerApp<T, D>> wrap(KafkaProducerApp<T, D> app) {
		return new DefaultPooledObject<KafkaProducerApp<T,D>>(app);
	}
}
