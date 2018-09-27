package com.fan.analysis.tools;

import org.apache.commons.lang.StringUtils;

import com.fan.analysis.bean.ETLTrackBean;
import com.fan.analysis.bean.LogBean;
import com.fan.analysis.utils.GetObjectToClassUtils;

/**
 * @description 解析etl之后的日志，封装成为一个对象
 * @author fan
 *
 */
public class ETLTrackAnalysis extends CommonAnalysis {

	@Override
	protected LogBean analysis(String line) {
		ETLTrackBean bean=null;
		if(StringUtils.isNotBlank(line)){
			String[] lines=line.split("\001", -1);
			if(lines!=null&&lines.length>=28){
				bean=(ETLTrackBean)GetObjectToClassUtils
						.getObjectByArray(ETLTrackBean.class, lines);
			}
		}
		return bean;
	}

}
