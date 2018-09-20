package com.vdata.track.storm.bolt;


import com.vdata.analysis.tools.CommonAnalysisToolsDriver;
import com.vdata.analysis.tools.LogToolsTag;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * @author fan
 * @description 日志解析逻辑bolt
 */
public class TrackAnalysisBolt extends BaseRichBolt {
    //引入log4j进行日志处理
//	private final Logger LOG=Logger.getLogger(TrackAnalysisBolt.class);
    private OutputCollector collector = null;

    @Override
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        //从kafkaspout中读取track原始日志
        try {
            String line = tuple.getString(0);
            if (StringUtils.isNotBlank(line)) {
                //适应同时传输过来多行数据的情况
                String[] lines = line.split("\n", -1);
                StringBuffer buffer = new StringBuffer();//idea提示修改，原来是stringbuffer
                for (String tmpLine : lines) {
                    String jsonLine = CommonAnalysisToolsDriver.parserToJson(tmpLine, LogToolsTag.TRACK_ORIGINAL_LOG);
//					if(StringUtils.isNotBlank(jsonLine)){
//						collector.emit(new Values(jsonLine));
//					}
                    buffer.append(jsonLine);
                    buffer.append("\n");
                }

                //将数据块发射到下一个bolt中
                collector.emit(new Values(buffer.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
//			LOG.error(e);
        } finally {
            collector.ack(tuple);
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }

}
