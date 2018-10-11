package com.old_kafka_storm_cycle_case4.analysis.tools;

import com.old_kafka_storm_cycle_case4.analysis.bean.LogBean;

/**
 * @description 日志逻辑解析接口
 * @author fan
 *
 */
public abstract class CommonAnalysis {
	
	/**
	 * @description 日志解析方法
	 * @param line 日志
	 * @return 返回一个日志解析完成后的实体类
	 */
	protected abstract LogBean analysis(String line);
}
