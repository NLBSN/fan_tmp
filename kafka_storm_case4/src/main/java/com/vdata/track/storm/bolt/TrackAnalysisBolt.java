package com.vdata.track.storm.bolt;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.vdata.analysis.tools.CommonAnalysisToolsDriver;
import com.vdata.analysis.tools.LogToolsTag;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * @description 日志解析逻辑bolt
 * @author tzc
 *
 */
public class TrackAnalysisBolt extends BaseRichBolt {
	//引入log4j进行日志处理
//	private final Logger LOG=Logger.getLogger(TrackAnalysisBolt.class);
	private OutputCollector collector=null;
	
	@Override
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector=collector;
	}
	
	@Override
	public void execute(Tuple tuple) {
		//从kafkaspout中读取track原始日志
		try {
			String line=tuple.getString(0);
			if(StringUtils.isNotBlank(line)){
				//适应同时传输过来多行数据的情况
				String[] lines=line.split("\n", -1);
				StringBuffer buffer=new StringBuffer();
				for(String tmpLine:lines){
					String jsonLine=CommonAnalysisToolsDriver
								.parserToJson(tmpLine, LogToolsTag.TRACK_ORIGINAL_LOG);
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
		} finally{
			collector.ack(tuple);
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("message"));
	}

}
