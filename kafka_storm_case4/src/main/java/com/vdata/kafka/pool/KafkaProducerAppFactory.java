package com.vdata.kafka.pool;

/**
 * @description 抽象工作类
 * @author tzc
 *
 */
public abstract class KafkaProducerAppFactory<T, D> {

	/**
	 * @description 生成连接点的方法
	 * @return 返回一个连接点
	 */
	public abstract KafkaProducerApp<T, D> newInstance();
}
