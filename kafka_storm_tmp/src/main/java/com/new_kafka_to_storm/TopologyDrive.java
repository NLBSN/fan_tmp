package com.new_kafka_to_storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Properties;


/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/10/9 14:33
 * @Modified By:
 */
public class TopologyDrive {
    // 引入log4j
    // private final static Logger log = Logger.getLogger(TopologyDrive.class);
    // kafka消费者的zookeeper地址
    private final static String KAFKA_CONSUMER_ZOOKEEPER = "kafka.consumer.zookeeper";
    // kafka消费的topic
    private final static String KAFKA_CONSUMER_TOPIC = "kafka.consumer.topic";
    // kafka消费者偏移量在zookeeper上的存储路径
    private final static String KAFKA_CONSUMER_OFFERSET_DIR = "kafka.consumer.offerset.dir";
    // kafka消费者的group id
    private final static String KAFKA_CONSUMER_GROUP_ID = "kafka.consumer.group.id";
    // kafka生产者的连接
    public final static String KAFKA_PRODUCER_SERVER = "kafka.producer.server";
    // kafka生产者的topic
    public final static String KAFKA_PRODUCER_TOPIC = "kafka.producer.topic";

    // top的名称
    private final static String TOPOLOGY_NAME = "topology.name";

    // 可选参数
    // kafkaspout执行的线程数
    private final static String TOP_KAFKA_SPOUT_EXCUTOR_NUMBER = "top.kafka.spout.excutor.number";
    private final static String DEFAULT_TOP_KAFKA_SPOUT_EXCUTOR_NUMBER = "3";
    // 解析bolt执行的线程数
    // private final static String TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER = "top.analysis.bolt.excutor.number";
    // private final static String DEFAULT_TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER = "3";
    // 写入kafka bolt执行的线程数
    private final static String TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER = "top.tokafka.bolt.excutor.number";
    private final static String DEFAULT_TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER = "1";
    // top的work数
    private final static String TOP_WORKER_NUMBER = "top.worker.number";
    private final static String DEFAULT_TOP_WORKER_NUMBER = "2";

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        //两种不同的配置文件写法
        //第一种写法，直接从kafka的消费者copy过来
        Properties props =new Properties();
        // props.put("bootstrap.servers", "10.16.48.44:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaSpoutConfig<String, String> kafkaBuilder = KafkaSpoutConfig.builder("10.16.48.44:9092", "vdata").setProp(props).build();

        //第二种写法
        // KafkaSpoutConfig<String, String> kafkaBuilder = KafkaSpoutConfig.builder("10.16.48.44:9092", "vdata")
                // .setProp(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true")
                // .setProp(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,1000)
                // .setProp(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,30000)
                // .setProp(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                // .setProp(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class)
                // .setOffsetCommitPeriodMs(10000)//控制spout多久向kafka提交一次offset
                // .setGroupId("testgroup")    //设置属于哪一个组
                // .setMaxUncommittedOffsets(250)
                // .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.LATEST)
                // .build();

        //通过kafkaspoutconfig获得kafkaspout
        KafkaSpout<String,String> kafkaSpout = new KafkaSpout<>(kafkaBuilder);
        //设置几个线程接收数据
        builder.setSpout("kafkaSpout", kafkaSpout, 1);
        //设置几个线程处理数据
        builder.setBolt("printBolt", new PrintBolr(), 1).shuffleGrouping("kafkaSpout");

        Config config = new Config();
        config.setDebug(true);
        config.setNumWorkers(1);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("testKafkaStrom", config, builder.createTopology());

        // 判断传入的参数个数是否符合预期
        // if (args.length < 0) {
        //     throw new RuntimeException("**********************请输入正确的文件路径***********************");
        // }
        // 配置文件的名称
        // String configFileName = args[0];
        // Properties prop = new Utils().getProperties("config.properties");



        // 动态化配置kafkaspout所需的参数
        // BrokerHosts hosts = new ZkHosts(prop.getProperty(KAFKA_CONSUMER_ZOOKEEPER));
        // SpoutConfig spoutConfig = new SpoutConfig(hosts, prop.getProperty(KAFKA_CONSUMER_TOPIC), prop.getProperty(KAFKA_CONSUMER_OFFERSET_DIR), prop.getProperty(KAFKA_CONSUMER_GROUP_ID));
        // spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        // KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        // top设置kafkaspout
        // builder.setSpout("producerSpout", kafkaSpout, Integer.parseInt(prop.getProperty(TOP_KAFKA_SPOUT_EXCUTOR_NUMBER, DEFAULT_TOP_KAFKA_SPOUT_EXCUTOR_NUMBER)));
        // top设置解析的bolt
        // builder.setBolt("analysisBolt", new TrackAnalysisBolt(), Integer.parseInt(prop.getProperty(TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER, DEFAULT_TOP_ANALYSIS_BOLT_EXCUTOR_NUMBER))).shuffleGrouping("kafkaSpout");
        // 设置写kafka的bolt
        // builder.setBolt("testBolt", new MyBolt(), Integer.parseInt(prop.getProperty(TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER, DEFAULT_TOP_TOKAFKA_BOLT_EXCUTOR_NUMBER))).shuffleGrouping("analysisBolt");


        // Config conf = new Config();
        // 将kafka生产者所需要的参数通过config传到bolt中
        // conf.put(KAFKA_PRODUCER_SERVER, prop.getProperty(KAFKA_PRODUCER_SERVER));
        // conf.put(KAFKA_PRODUCER_TOPIC, prop.getProperty(KAFKA_PRODUCER_TOPIC));
        // conf.setNumWorkers(Integer.parseInt(prop.getProperty(TOP_WORKER_NUMBER, DEFAULT_TOP_WORKER_NUMBER)));
        // conf.put(Config.TOPOLOGY_DEBUG, true);//用于调试
        // 在本地运行
        // LocalCluster cluster = new LocalCluster();
        // cluster.submitTopology("mytopology", conf, builder.createTopology());

        // 在集群上运行
        // StormSubmitter.submitTopology("mytopology", conf, builder.createTopology());
    }


}