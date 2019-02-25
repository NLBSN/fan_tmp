package com.old_kafka_storm_cycle_case4.analysis.tools;

import com.old_kafka_storm_cycle_case4.analysis.bean.LogBean;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fan
 * @description 日志解析驱动类
 */
public class CommonAnalysisToolsDriver {
    private static Map<LogToolsTag, CommonAnalysis> map = null;

    /**
     * @param logline 日志
     * @param tag     日志解析标识
     * @return 返回一个日志解析完成后的实体对象
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @description 日志解析统一的接口方法
     */
    public static LogBean parserToInstance(String logline, LogToolsTag tag) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        if (map == null)
            map = new HashMap<LogToolsTag, CommonAnalysis>();

        CommonAnalysis analysis = map.get(tag);
        if (analysis == null) {
            String toolTag = tag.getTag();
            Class<?> claz = Class.forName(toolTag);
            analysis = (CommonAnalysis) claz.newInstance();
            map.put(tag, analysis);
        }

        return analysis.analysis(logline);
    }

    public static String parserToJson(String logline, LogToolsTag tag)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (map == null) {
            map = new HashMap();
        }
        CommonAnalysis analysis = (CommonAnalysis) map.get(tag);
        if (analysis == null) {
            String toolTag = tag.getTag();
            Class claz = Class.forName(toolTag);
            analysis = (CommonAnalysis) claz.newInstance();
            map.put(tag, analysis);
        }

        LogBean bean = analysis.analysis(logline);
        String jsonLine = null;

        if (bean != null) {
            jsonLine = JSONObject.fromObject(bean).toString();//原来的
//			jsonLine = JSON.toJSONString(bean).toString();
        }
        return jsonLine;
    }
}
