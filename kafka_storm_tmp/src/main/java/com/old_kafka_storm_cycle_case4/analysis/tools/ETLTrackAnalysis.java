package com.old_kafka_storm_cycle_case4.analysis.tools;

import org.apache.commons.lang.StringUtils;

import com.old_kafka_storm_cycle_case4.analysis.bean.ETLTrackBean;
import com.old_kafka_storm_cycle_case4.analysis.bean.LogBean;
import com.old_kafka_storm_cycle_case4.analysis.utils.GetObjectToClassUtils;

/**
 * @author fan
 * @description 解析etl之后的日志，封装成为一个对象
 */
public class ETLTrackAnalysis extends CommonAnalysis {

    @Override
    protected LogBean analysis(String line) {
        ETLTrackBean bean = null;
        if (StringUtils.isNotBlank(line)) {
            String[] lines = line.split("\001", -1);
            if (lines != null && lines.length >= 28) {
                bean = (ETLTrackBean) GetObjectToClassUtils.getObjectByArray(ETLTrackBean.class, lines);
            }
        }
        return bean;
    }

}
