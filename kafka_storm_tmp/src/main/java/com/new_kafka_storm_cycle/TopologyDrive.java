package com.new_kafka_storm_cycle;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
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
    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        // 从kafka拉取数据到storm
        //两种不同的配置文件写法
        //第一种写法，直接从kafka的消费者copy过来
        Properties props = new Properties();
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaSpoutConfig<String, String> kafkaBuilder = KafkaSpoutConfig.builder("10.16.48.44:9092", "test").setProp(props).build();

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
        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaBuilder);
        //设置几个线程接收数据
        builder.setSpout("kafkaSpoutToStorm", kafkaSpout, 1);
        //设置几个线程处理数据
        // builder.setBolt("printBolt", new PrintBolr(), 1).shuffleGrouping("kafkaSpout");


        // storm发数据到kafka
        Properties propss = new Properties();
        propss.put("bootstrap.servers", "10.16.48.44:9092");
        propss.put("acks", "all");
        propss.put("retries", 0);
        propss.put("batch.size", 16384);
        propss.put("linger.ms", 1);
        propss.put("buffer.memory", 33554432);
        propss.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        propss.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        builder.setBolt("toKafkaBolt", new KafkaBolt<>().withProducerProperties(propss)
                .withTopicSelector("vdata")
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<>("", "value")), 1)
                .shuffleGrouping("kafkaSpoutToStorm");

        Config config = new Config();
        config.setDebug(true);
        config.setNumWorkers(1);
        // 在本地运行
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("kafka-toStorm-toKafka", config, builder.createTopology());

        // 在集群上运行
        // StormSubmitter.submitTopology("mytopology", conf, builder.createTopology());
    }


}