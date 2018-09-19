package reduce;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class TrackEtlReducer extends Reducer<Text, Text, NullWritable, Text> {
	private Map<String, String> spamIpMap=null;
	private final Long INTERVALTIME = 1000 * 60 * 30L; // 间隔时间 1800000 ms
	
	@Override
	protected void setup(
			Reducer<Text, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		
		BufferedReader buffer=null;
		
		Path[] paths=DistributedCache.getLocalCacheFiles(context.getConfiguration());
		for(Path tmpPath:paths){
			String pathStr=tmpPath.toString();
			if(pathStr.contains("spamIp")){
				spamIpMap=new HashMap<String, String>();
				
				buffer=new BufferedReader(new InputStreamReader(
						new FileInputStream(pathStr), "utf-8"));
				while(buffer.ready()){
					String tmpLine=buffer.readLine();
					if(StringUtils.isNotBlank(tmpLine)){
						String[] lines=tmpLine.split(" ", -1);
						if(lines!=null&&lines.length>=2){
							String tmpKey=lines[0];
							String tmpValue=lines[1];
							if(StringUtils.isNotBlank(tmpKey)&&
									StringUtils.isNotBlank(tmpValue)){
								spamIpMap.put(tmpKey, tmpValue);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void reduce(Text key, Iterable<Text> value,
			Reducer<Text, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		String tmpKey=key.toString();
		Map<Long, List<String>> mapSessions = splitCookiesToSession(value);
			
		if(StringUtils.isNotBlank(tmpKey)){
			if (mapSessions != null && mapSessions.size() != 0) {
				int mapLength = mapSessions.size();
				int mapStep = 0;
				for (List<String> listText : mapSessions.values()) {
					if (listText != null && listText.size() != 0) {
						++mapStep;
						int listLength = listText.size();
						int listStep = 0;
						for (String textTemp : listText) {
							++listStep;
							String tmpValue = null;
							if (mapLength == mapStep && listLength == listStep) {
								tmpValue = textTemp + "\001" + mapStep + "\001"
										+ mapStep + "end";
							} else {
								tmpValue = textTemp + "\001" + mapStep + "\001"
										+ mapStep;
							}
							tmpValue = tmpValue.replace("\n", "").replace("\r", "");
							tmpValue = tmpValue.replace("\\n", "").replace("\\r",
									"");
							context.write(NullWritable.get(), new Text(tmpValue));
						}
					} else {
						mapLength--;
					}
				}
			}
		}
	}
	
	/* cookies分组成session */
	private Map<Long, List<String>> splitCookiesToSession(Iterable<Text> values) {
		// 返回的结果
		Map<Long, List<String>> result = new TreeMap<Long, List<String>>();

		Map<Long, String> treeMap = new TreeMap<Long, String>();
		int flag = 0;
		for (Text textValue : values) {
			if (flag > 100000) {
				return result;
			}
			try {
				String line = textValue.toString();
				String[] lines = line.split("\002", -1);
				if (spamIpMap != null && spamIpMap.size() != 0) {
					String userIp = lines[1];
					if (spamIpMap.containsKey(userIp)) {
						continue;
					}
				}
				long epoch = Long.parseLong(lines[2]);
				
				treeMap.put(epoch, lines[0]);
			} catch (Exception e) {// 一些特殊数据
				throw new RuntimeException(e);
			}
			flag++;
		}
		
		Long startTime = null;
		Long tempSession = null;
		for (Entry<Long, String> entry : treeMap.entrySet()) {
			if (startTime == null) {
				startTime = entry.getKey();
				tempSession = entry.getKey();
				List<String> tempList = new ArrayList<String>();
				tempList.add(entry.getValue());
				result.put(tempSession, tempList);
				continue;
			}
			/* 如果两个时间间隔超过30分钟 */
			if (entry.getKey() - startTime >= INTERVALTIME) {
				tempSession = entry.getKey();
				List<String> tempList = new ArrayList<String>();
				tempList.add(entry.getValue());
				result.put(tempSession, tempList);
			} else {
				/* 没有超过30分钟 */
				List<String> tempSet = result.get(tempSession);
				tempSet.add(entry.getValue());
				result.put(tempSession, tempSet);
			}
			startTime = entry.getKey();
		}
		return result;
	}
	
}
