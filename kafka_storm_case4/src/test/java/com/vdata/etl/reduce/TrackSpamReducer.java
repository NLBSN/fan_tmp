package com.vdata.etl.reduce;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

/**
 * @description 反爬虫过滤的reduce阶段，需要计算pv/uv的比值，然后过滤掉爬虫
 * @author tzc
 *
 */
public class TrackSpamReducer extends Reducer<Text, Text, NullWritable, Text> {
	
	@Override
	protected void reduce(Text key, Iterable<Text> value,
			Reducer<Text, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		String userip=key.toString();
		Set<String> cookieIdSet=new HashSet<String>();
		int pv=0;//用于统计pv
		for(Text tmp:value){
			String cookieID=tmp.toString();
			cookieIdSet.add(cookieID);
			pv++;
		}
		
		int uv=cookieIdSet.size();//计算uv
		
		if(pv!=0&&uv!=0){
			double proportion=pv/uv;//比值
			/**
			 * 是爬虫的判断条件
			 * 1、pv至少是大于100的，才认为有可能是机器操作的
			 * 2、pv/uv的比值最小值小于等于1.12
			 * 3、pv/uv的比值最大值不能超过500
			 */
			if(pv>100&&(proportion<=1.12||proportion>500)){
				String tmpValue=userip+" "+proportion+" "+pv+" "+uv;
				context.write(NullWritable.get(), new Text(tmpValue));
			}
		}
		
		//12.123.11.101 1.21 pv uv
	}
}
