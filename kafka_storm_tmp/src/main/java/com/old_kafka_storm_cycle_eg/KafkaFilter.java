package com.old_kafka_storm_cycle_eg;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Properties;

@SuppressWarnings("deprecation")
public class KafkaFilter {
    public static void main(String[] args) throws Exception {
        String brokerZkStr = "risbig14:2181,risbig15:2181,risbig16:2181";

        String brokerZkPath = "/kafka/brokers";//topic在zookeeper上根目录
        String topicIn = "topic1"; //输入topic的名称,完整的目录为/kafka/brokers/topics/topic1
        String topicOut = "topic2";//输出topic的名称,完整的目录为/kafka/brokers/topics/topic2

        String zkRoot = "/kafka";//用来保存消费者的偏离值

        String id = "kafkafilterspout";//spout的唯一标志

        BrokerHosts brokerHosts = new ZkHosts(brokerZkStr, brokerZkPath);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topicIn, zkRoot, id);

        //设置生产者的属性信息
        Properties props = new Properties();
        props.put("bootstrap.servers", "risbig14:9092");
        props.put("acks", "1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        spoutConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-filter-spout", new KafkaSpout(spoutConfig));
        // 按照数据源类型发射多个流，暂时是两个
        builder.setBolt("bolt-type-filter", new BoltTypeFilter(), 5).noneGrouping("kafka-filter-spout");//declarer.declareStream("stream-news-filter", new Fields("TYPE", "JSONOBJECT"));
        // 处理新闻数据
        builder.setBolt("bolt-news-filter", new BoltNewsFilter(), 2).shuffleGrouping("bolt-type-filter", "stream-news-filter");//declarer.declareStream("stream-keywords-filter", new Fields("message"));
        // write to kafka
        builder.setBolt("bolt-kafka-filter", new KafkaBolt<String, Integer>()
                        .withProducerProperties(props)
                        .withTopicSelector(new DefaultTopicSelector(topicOut))
                        .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper()))
                .shuffleGrouping("bolt-news-filter", "stream-relatedsite-filter-result");

        Config conf = new Config();
        conf.setNumWorkers(3);
        StormSubmitter.submitTopology(args[0], conf, builder.createTopology());

    }
}
