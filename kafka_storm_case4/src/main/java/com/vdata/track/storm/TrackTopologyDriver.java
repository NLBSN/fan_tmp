package com.vdata.track.storm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.vdata.track.storm.bolt.ToKafkaBolt;
import com.vdata.track.storm.bolt.TrackAnalysisBolt;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

public class TrackTopologyDriver {
	//引入log4j
	private final static Logger LOG=Logger.getLogger(TrackTopologyDriver.class);
	//kafka消费者的zookeeper地址
	public final static String KAFKA_CONSUMER_ZOOKEEPER="kafka.consumer.zookeeper";
	//kafka消费的topic
	public final static String KAFKA_CONSUMER_TOPIC="kafka.consumer.topic";
	//kafka消费者偏移量在zookeeper上的存储路径
	public final static String KAFKA_CONSUMER_OFFERSET_DIR="kafka.consumer.offerset.dir";
	//kafka消费者的group id
	public final static String KAFKA_CONSUMER_GROUP_ID="kafka.consumer.group.id";
	//kafka生产者的连接
	public final static String KAFKA_PRODUCER_SERVER="kafka.producer.server";
	//kafka生产者的topic
	public final static String KAFKA_PRODUCER_TOPIC="kafka.producer.topic";
	
	//top的名称
	public final static String TOPOLOGY_NAME="topology.name";
	
	
	//可选参数
	//kafkaspout执行的线程数
	public final static String TOP_KAFKA_SPOUT_EXCUTOR_NUMBER="top.kafka.spout.excutor.number";
	private final static String DEFAULT_TOP_KAFKA_SPOUT_EXCUTOR_NUMBER="3";
	//解析bolt执行的线程数
	public final static String TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER="top.analysis.bolt.excutor.number";
	private final static String DEFAULT_TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER="3";
	//写入kafka bolt执行的线程数
	public final static String TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER="top.tokafka.bolt.excutor.number";
	private final static String DEFAULT_TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER="1";
	//top的work数
	public final static String TOP_WORKER_NUMBER="top.worker.number";
	private final static String DEFAULT_TOP_WORKER_NUMBER="2";
	
	public static void main(String[] args) throws Exception {
		if(args.length<1)
			throw new RuntimeException("******please input filename**************");
		//配置文件的名称
		String configFileName=args[0];
		Properties prop=getProperties(configFileName);
		
		TopologyBuilder builder = new TopologyBuilder();
		//动态化配置kafkaspout所需要的参数
		BrokerHosts hosts = new ZkHosts(prop.getProperty(KAFKA_CONSUMER_ZOOKEEPER));
		SpoutConfig spoutConfig = new SpoutConfig(hosts, prop.getProperty(KAFKA_CONSUMER_TOPIC),
										prop.getProperty(KAFKA_CONSUMER_OFFERSET_DIR),
										prop.getProperty(KAFKA_CONSUMER_GROUP_ID));
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		
		//top设置kafkaspout
		builder.setSpout("kafkaSpout", kafkaSpout,Integer.parseInt(
				prop.getProperty(TOP_KAFKA_SPOUT_EXCUTOR_NUMBER, 
							DEFAULT_TOP_KAFKA_SPOUT_EXCUTOR_NUMBER)));
		//top设置解析的bolt
		builder.setBolt("analysisBolt", new TrackAnalysisBolt(),Integer.parseInt(
				prop.getProperty(TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER,
						DEFAULT_TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER)))
				.shuffleGrouping("kafkaSpout");
		//设置写kafka的bolt
		builder.setBolt("toKafkaBolt", new ToKafkaBolt(),Integer.parseInt(
				prop.getProperty(TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER,
						DEFAULT_TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER)))
				.shuffleGrouping("analysisBolt");
		
		Config conf=new Config();
		//将kafka生产者所需要的参数通过config传到bolt中
		conf.put(KAFKA_PRODUCER_SERVER, prop.getProperty(KAFKA_PRODUCER_SERVER));
		conf.put(KAFKA_PRODUCER_TOPIC, prop.getProperty(KAFKA_PRODUCER_TOPIC));
		
		conf.setNumWorkers(Integer.parseInt(
				prop.getProperty(TOP_WORKER_NUMBER,DEFAULT_TOP_WORKER_NUMBER)));
		conf.put(Config.TOPOLOGY_DEBUG, true);//用于调试
		
		//在本地运行的方式
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(prop.getProperty(TOPOLOGY_NAME), conf, builder.createTopology());
				
//		StormSubmitter.submitTopology(prop.getProperty(TOPOLOGY_NAME), conf, builder.createTopology());

	}
	
	//读取配置文件
	private static Properties getProperties(String confPath) throws Exception{
		InputStream in = TrackTopologyDriver.class.getClassLoader().getResourceAsStream(confPath);
		Properties prop = new Properties();
		try {
			prop.load(in);//获取配置文件
			in.close();
		} catch (IOException e) {
			LOG.error("No config.properties defined error");
		}
		return prop;
	}

}
