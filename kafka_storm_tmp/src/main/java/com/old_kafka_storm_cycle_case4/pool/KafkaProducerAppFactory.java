package com.old_kafka_storm_cycle_case4.pool;

/**
 * @description 抽象工作类
 * @author fan
 *
 */
public abstract class KafkaProducerAppFactory<T, D> {

	/**
	 * @description 生成连接点的方法
	 * @return 返回一个连接点
	 */
	public abstract KafkaProducerApp<T, D> newInstance();
}
