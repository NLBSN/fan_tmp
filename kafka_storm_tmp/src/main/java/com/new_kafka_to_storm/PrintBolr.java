package com.new_kafka_to_storm;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/10/10 14:45
 * @Modified By:
 */
public class PrintBolr extends BaseBasicBolt {
    private BasicOutputCollector collector = null;
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {

        System.out.println(tuple.getStringByField("value"));

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
