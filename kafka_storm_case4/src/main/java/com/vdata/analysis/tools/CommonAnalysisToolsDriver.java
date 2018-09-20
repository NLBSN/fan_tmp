package com.vdata.analysis.tools;

import com.vdata.analysis.bean.LogBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 日志解析驱动类
 * @author fan
 *
 */
public class CommonAnalysisToolsDriver {
	private static Map<LogToolsTag, CommonAnalysis> map=null;

	/**
	 * @description 日志解析统一的接口方法
	 * @param logline 日志
	 * @param tag 日志解析标识
	 * @return 返回一个日志解析完成后的实体对象
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static LogBean parserToInstance(String logline,LogToolsTag tag) throws ClassNotFoundException,
				InstantiationException, IllegalAccessException{
		if(map==null)
			map=new HashMap<LogToolsTag, CommonAnalysis>();
		
		CommonAnalysis analysis=map.get(tag);
		if(analysis==null){
			String toolTag=tag.getTag();
			Class<?> claz=Class.forName(toolTag);
			analysis=(CommonAnalysis)claz.newInstance();
			map.put(tag, analysis);
		}
		
		return analysis.analysis(logline);
	}
}
