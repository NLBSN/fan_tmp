package com.vdata.etl.map;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.vdata.analysis.tools.CommonAnalysisToolsDriver;
import com.vdata.analysis.tools.LogToolsTag;
import com.vdata.analysis.tools.OriginalTrackBean;


public class TrackEtlMapper extends Mapper<LongWritable, Text, Text, Text> {
	private Map<String, String> cateDicMap=null;//字典表
	
	@Override
	protected void setup(
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		BufferedReader buffer=null;
		
		Path[] paths=DistributedCache.getLocalCacheFiles(context.getConfiguration());
		for(Path tmpPath:paths){
			String pathStr=tmpPath.toString();
			if(pathStr.contains("cateflag")){
				cateDicMap=new HashMap<String, String>();
				
				buffer=new BufferedReader(new InputStreamReader(
						new FileInputStream(pathStr), "utf-8"));
				while(buffer.ready()){
					String tmpLine=buffer.readLine();
					if(StringUtils.isNotBlank(tmpLine)){
						String[] lines=tmpLine.split("	", -1);
						if(lines!=null&&lines.length>=3){
							String tmpValue=lines[0];
							String tmpKey=lines[1];
							cateDicMap.put(tmpKey, tmpValue);
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		//运行时候调用
		String line=value.toString();
		if(StringUtils.isNotBlank(line)){
			try {
				OriginalTrackBean bean=(OriginalTrackBean)CommonAnalysisToolsDriver
						.parserToInstance(line, LogToolsTag.TRACK_ORIGINAL_LOG);
				if(bean!=null){
					String biz="other";
					String catePath=bean.getCatePath();
					if(StringUtils.isNotBlank(catePath)){
						String[] catePaths=catePath.split(",", -1);
						String tmpCateKey=catePaths[catePaths.length-1];
						String tmpBiz=cateDicMap.get(tmpCateKey);
						if(StringUtils.isNotBlank(tmpBiz))
							biz=tmpBiz;
					}
					bean.setBizName(biz);//设置业务线
					
					String cookieId=bean.getCookieID();
					String userIp=bean.getUserIp();
					//得到日志的组合
					String beanLine=bean.toEtlString();
					String epoch=bean.getEpoch();
					String tmpEpoch=epoch.replace(".", "");
					String tmpValue=beanLine+"\002"+userIp+"\002"+tmpEpoch;
					
					context.write(new Text(cookieId), new Text(tmpValue));
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
	}
}
