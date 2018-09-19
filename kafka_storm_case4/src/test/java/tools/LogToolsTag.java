package tools;

/**
 * @description 用于保存日志解析标识
 * @author tzc
 *
 */
public enum LogToolsTag {
	TRACK_ORIGINAL_LOG{//track日志解析标识
		@Override
		public String getTag() {
			return "com.vdata.analysis.tools.OriginalTrackAnalysis";
		}
	};
	
	public abstract String getTag();// 获取标志
}
