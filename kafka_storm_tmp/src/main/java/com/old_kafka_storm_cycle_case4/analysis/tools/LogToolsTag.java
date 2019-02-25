package com.old_kafka_storm_cycle_case4.analysis.tools;

/**
 * @description 用于保存日志解析标识
 * @author fan
 *
 */
public enum LogToolsTag {
	TRACK_ORIGINAL_LOG{//track日志解析标识
		@Override
		public String getTag() {
			return "com.fan.com.fan.tools.OriginalTrackAnalysis";
		}
	},

	TRACK_ETL_LOG{//track日志解析标识
		@Override
		public String getTag() {
			return "com.fan.com.fan.tools.ETLTrackAnalysis";
		}
	};

	public abstract String getTag();// 获取标志
}
