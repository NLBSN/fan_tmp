package com.new_storm_single;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/10/9 14:33
 * @Modified By:
 */
public class MyBolt extends BaseRichBolt {
    @Override
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public void execute(Tuple tuple) {
        String word = tuple.getStringByField("word");
        System.out.println("---------------------------" + word);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
