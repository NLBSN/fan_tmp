package com.new_storm_single;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/10/9 14:33
 * @Modified By:
 */
public class Mysput extends BaseRichSpout {
    private SpoutOutputCollector collector = null;

    @Override
    public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
        this.collector=collector;
    }

    @Override
    public void nextTuple() {
        String[] line = {"wangbaduzi","liheiguilaogui","lanlanlajlajlfj"};
        for (String tmp : line){
            this.collector.emit(new Values(tmp));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}
