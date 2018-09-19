package map;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.vdata.analysis.tools.CommonAnalysisToolsDriver;
import com.vdata.analysis.tools.LogToolsTag;
import com.vdata.analysis.tools.OriginalTrackBean;

/**
 * @description 反爬虫的map阶段
 * @author tzc
 *
 */
public class TrackSpamMapper extends Mapper<LongWritable, Text, Text, Text> {
	
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
					String userIp=bean.getUserIp();
					String cookieId=bean.getCookieID();
					if(StringUtils.isNotBlank(userIp)){
						context.write(new Text(userIp), new Text(cookieId));
					}
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
	}
}
