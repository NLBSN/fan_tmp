package com.vdata.analysis.tools;

import org.apache.commons.lang.StringUtils;

import com.vdata.analysis.bean.ETLTrackBean;
import com.vdata.analysis.bean.LogBean;
import com.vdata.analysis.utils.GetObjectToClassUtils;

/**
 * @description 解析etl之后的日志，封装成为一个对象
 * @author tzc
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
