package com.fan.tools;

import com.fan.bean.ETLTrackBean;
import com.fan.utils.GetObjectToClassUtils;
import org.apache.commons.lang.StringUtils;

public class ETLTrackAnalysis extends CommonAnalysis {

    @Override
    protected LogBean analysis(String line) {
        ETLTrackBean bean = null;
        if (StringUtils.isNotBlank(line)) {
            String[] lines = line.split("\001", -1);
            if ((lines != null) && (lines.length >= 28)) {
                bean = (ETLTrackBean)
                        GetObjectToClassUtils.getObjectByArray(ETLTrackBean.class, lines);
            }
        }
        return bean;
    }
}