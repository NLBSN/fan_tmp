package com.new_storm_single;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

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
        builder.setSpout("producerSpout", new Mysput(),1);
        builder.setBolt("getDataBolt", new MyBolt(), 1).shuffleGrouping("producerSpout");
        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.put(Config.TOPOLOGY_DEBUG, false);
        //在本地运行
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("mytopology", conf, builder.createTopology());

        //在集群上运行
//        StormSubmitter.submitTopology("mytopology", conf, builder.createTopology());
    }


}