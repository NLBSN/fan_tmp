package com.old_kafka_storm_cycle_case4.analysis.utils;

import java.util.Calendar;
import java.util.Random;

/**
 * @description 用于重新获取随机cookieid
 * @author fan
 *
 */
public class CookieIdUtils {
	private static final String PREFIX = "Z_NULL_";
	private static Random random = new Random();
	private static Calendar calendar = Calendar.getInstance();
	
	/**
	 * @description 获取一个随机cookieId
	 * @return 返回一个随机cookieId
	 */
	public static String getCookieIdOfRand(){
		long middle=calendar.getTime().getTime();
		int suffix=Math.abs(random.nextInt() );
		return PREFIX + middle+suffix;
//		return PREFIX+"_"+Math.abs(random.nextInt());
	}
	
}
