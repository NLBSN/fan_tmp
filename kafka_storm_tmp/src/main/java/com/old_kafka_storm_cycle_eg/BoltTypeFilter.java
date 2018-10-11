package com.old_kafka_storm_cycle_eg;

import com.alibaba.fastjson.JSONObject;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class BoltTypeFilter implements IBasicBolt {

    /**
     *
     */
    private static final long serialVersionUID = -6124126300235524121L;

    public void execute(Tuple input, BasicOutputCollector collector) {
        String document = input.getString(0);
        JSONObject object = JSONObject.parseObject(document);
        int type = object.getIntValue("TYPE");
        switch (type) {
            case 1:
                collector.emit("stream-news-filter", new Values(type, object));
                break;
            default:
                System.out.println();
                break;
        }

    }

    public void cleanup() {
        // TODO Auto-generated method stub

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("stream-news-filter", new Fields("TYPE", "JSONOBJECT"));
    }

    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    public void prepare(Map stormConf, TopologyContext context) {
        // TODO Auto-generated method stub

    }

}
