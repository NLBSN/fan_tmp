package com.fan.tools;

/**
 * @description 日志逻辑解析接口
 * @author tzc
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
