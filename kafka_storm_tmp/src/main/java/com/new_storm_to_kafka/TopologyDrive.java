package com.new_storm_to_kafka;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Properties;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/10/9 14:33
 * @Modified By:
 */
public class TopologyDrive {
    //本地运行
    public static void main (String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();

        Properties props = new Properties();
        props.put("bootstrap.servers", "10.16.48.44:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.put(Config.TOPOLOGY_DEBUG, false);

        builder.setSpout("producerSpout", new Mysput(),1);
        // builder.setBolt("getDataBolt", new MyBolt(), 1).shuffleGrouping("producerSpout");
        builder.setBolt("toKafka", new KafkaBolt<>().withProducerProperties(props)
                .withTopicSelector("vdata")
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<>("", "word"))
                , 1).shuffleGrouping("producerSpout");

        //在本地运行
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("mytopology", conf, builder.createTopology());

        //在集群上运行
//        StormSubmitter.submitTopology("mytopology", conf, builder.createTopology());
    }


}