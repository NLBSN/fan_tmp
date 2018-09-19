package com.vdata.track.storm.bolt;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.vdata.kafka.pool.KafkaPool;
import com.vdata.kafka.pool.KafkaProducerApp;
import com.vdata.track.storm.TrackTopologyDriver;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * @description 将数据写入kafka中
 * @author tzc
 *
 */
public class ToKafkaBolt extends BaseRichBolt {
//	private final Logger LOG=Logger.getLogger(ToKafkaBolt.class);
	//kafka连接池
	private KafkaPool<String, String> pool=null;
	private String topic=null;//kafka中主题

	@Override
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		//h获取kafka生产者的连接
		String kafka_producer_server=map.get(TrackTopologyDriver.KAFKA_PRODUCER_SERVER).toString();
		initPool(kafka_producer_server);//初始化连接池
		//kafka生产者的topic
		topic=map.get(TrackTopologyDriver.KAFKA_PRODUCER_TOPIC).toString();
	}
	
	//初始化连接池
	private void initPool(String server){
		if(StringUtils.isNotBlank(server)){
			Properties props=new Properties();
			//将生产者的连接设置到properties中
			props.put("bootstrap.servers", server);
			props.put("acks", "all");
			props.put("retries", 0);
			props.put("batch.size", 16384);
			props.put("linger.ms", 1);
			props.put("buffer.memory", 33554432);
			props.put("key.serializer",
					"org.apache.kafka.common.serialization.StringSerializer");
			props.put("value.serializer",
					"org.apache.kafka.common.serialization.StringSerializer");
		
			pool=new KafkaPool<String,String>(props);
		}
	}
	
	@Override
	public void execute(Tuple tuple) {
		try {
			String line=tuple.getStringByField("message");
			if(StringUtils.isNotBlank(line)){
				String[] lines=line.split("\n", -1);
				KafkaProducerApp<String, String> producer=pool.borrowProducer();
				for(String tmpLine:lines){
					//将数据写到kafka中
					if(StringUtils.isNotBlank(tmpLine))
						producer.send(topic, tmpLine);
				}
				
				//写完数据后需要将连接点还到连接池中
				pool.returnProducer(producer);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			LOG.error(e);
		}
	}
	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

	@Override
	public void cleanup() {
		//资源关闭
		if(pool!=null)
			pool.close();
		
		super.cleanup();
	}

}
