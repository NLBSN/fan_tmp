package com.vdata.analysis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * @description 对时间进行解析
 * @author tzc
 *
 */
public class DateParserUtils {

	/**
	 * @description 得到起始范围内的日期列表，包含起始日期，可设置间隔天数
	 * @param startDate
	 *            开始时间，如20151010
	 * @param endDate
	 *            结束时间，如20151015
	 * @param intervalDay
	 *            间隔天数
	 * @return 返回一个标准的时间列表，如20151010,20151011
	 */
	public static List<String> getDateList(String startDate, String endDate,
			int intervalDay) {
		List<String> listDate = new ArrayList<String>();

		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Date dBegin;
		Date dEnd;
		try {
			dBegin = f.parse(startDate);
			dEnd = f.parse(endDate);
			if (dBegin.getTime() <= dEnd.getTime()) {
				for (long i = dBegin.getTime(); i <= dEnd.getTime(); i += 86400000 * (intervalDay + 1)) {
					Date d = new Date(i);
					String date = f.format(d);
					listDate.add(date);
				}
			} else {
				for (long i = dBegin.getTime(); i >= dEnd.getTime(); i -= 86400000 * (intervalDay + 1)) {
					Date d = new Date(i);
					String date = f.format(d);
					listDate.add(date);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return listDate;
	}

	/**
	 * @description 获取前n天的时间
	 * @param date 开始时间，如20151016
	 * @param nDaysAgo 相隔几天
	 * @return 返回n天前的时间，如20151010
	 */
	public static String getNDaysAgo(String date, int nDaysAgo) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date tmpDate=null;
		try {
			tmpDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long dateLong = tmpDate.getTime(); 
												
		calendar.setTime(new Date(dateLong));
		calendar.add(Calendar.DATE, -nDaysAgo);
		return format.format(calendar.getTime());
	}
	
	/**
	 * @description 获取格式化后的时间，如20160808
	 * @return 返回格式化后的时间
	 */
	public static String getToday(){
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String dateStr=format.format(date);
		return dateStr;
	}

	@Test
	public void test() {
		String day=getNDaysAgo("20151010",0);
		System.out.println(day);
	}
	
	@Test
	public void test1(){
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String dateStr=format.format(date);
		System.out.println(dateStr);
	}

}
