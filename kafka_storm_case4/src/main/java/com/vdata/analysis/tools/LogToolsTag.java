package com.vdata.analysis.tools;

/**
 * @description 用于保存日志解析标识
 * @author fan
 *
 */
public enum LogToolsTag {
	TRACK_ORIGINAL_LOG{//track日志解析标识
		@Override
		public String getTag() {
			return "com.vdata.analysis.tools.OriginalTrackAnalysis";
		}
	},
	
	TRACK_ETL_LOG{//track日志解析标识
		@Override
		public String getTag() {
			return "com.vdata.analysis.tools.ETLTrackAnalysis";
		}
	};
	
	public abstract String getTag();// 获取标志
}
