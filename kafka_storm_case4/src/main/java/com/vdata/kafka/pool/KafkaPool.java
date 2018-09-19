package com.vdata.kafka.pool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

/**
 * @description kafka连接池
 * @author tzc
 *
 */
public class KafkaPool<U, V> {
	private final Logger LOG=Logger.getLogger(KafkaPool.class);
	private GenericObjectPool<KafkaProducerApp<U, V>> pool=null;
	
	public KafkaPool(Properties properties){//初始化连接池
		//初始化出kafka生产者的工厂类
		KafkaProducerAppFactory<U, V> factory
				=new BaseKafkaProducerAppFactory<U, V>(properties);
		//初始化出连接池所需要的工厂类
		PooledObjectFactory<KafkaProducerApp<U, V>> poolFactory
				=new PooledKafkaProducerAppFactory<U, V>(factory);
		
		GenericObjectPoolConfig config=getConfig();
		this.pool=new GenericObjectPool<KafkaProducerApp<U, V>>(poolFactory, config);
	}
	
	//获取连接池的配置文件
	private GenericObjectPoolConfig getConfig(){
		GenericObjectPoolConfig config=new GenericObjectPoolConfig();
		Properties prop=getProperties("kafkaPool.properties");
		if(prop!=null&&prop.size()!=0){
			if(prop.contains("maxIdle")){
				String maxIdle=prop.getProperty("maxIdle");
				if(StringUtils.isNotBlank(maxIdle))
					config.setMaxIdle(Integer.parseInt(maxIdle));
			}
			
			if(prop.contains("maxTotal")){
				String maxTotal=prop.getProperty("maxTotal");
				if(StringUtils.isNotBlank(maxTotal))
					config.setMaxTotal(Integer.parseInt(maxTotal));
			}
			
			if(prop.contains("maxWaitMillis")){
				String maxWaitMillis=prop.getProperty("maxWaitMillis");
				if(StringUtils.isNotBlank(maxWaitMillis))
					config.setMaxWaitMillis(Integer.parseInt(maxWaitMillis));
			}
			
			//其它参数可以继续扩展
		}else{//设置一些默认的参数
			config.setMaxIdle(3);
			config.setMaxTotal(3);
		}
		return config;
	}
	
	//读取配置文件
	private Properties getProperties(String confPath){
		InputStream in = KafkaPool.class.getClassLoader().getResourceAsStream(confPath);
		Properties prop = new Properties();
		try {
			prop.load(in);//获取配置文件
			in.close();
		} catch (IOException e) {
			LOG.error("No config.properties defined error");
		}
		return prop;
	}
	
	/**
	 * @description 从连接池中获取生产者连接点
	 * @return 返回一个生产者的连接点
	 * @throws Exception 
	 */
	public KafkaProducerApp<U, V> borrowProducer() throws Exception{
		return this.pool.borrowObject();
	}
	
	/**
	 * @description 归还生产者连接点
	 * @param producer 生产者连接点
	 */
	public void returnProducer(KafkaProducerApp<U, V> producer){
		this.pool.returnObject(producer);
	}
	
	/**
	 * @description 将资源关闭
	 */
	public void close(){
		if(this.pool!=null)
			this.pool.close();
	}
}
