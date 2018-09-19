package com.vdata.analysis.utils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @description 将日志封装成对象的工具
 * @author tzc
 *
 */
public class GetObjectToClassUtils {

	/**
	 * @description 通过反射的方式将map中的参数封装到一个对象中
	 * @param claz
	 *            所需要封装的对象的class
	 * @param map
	 *            map参数集合，需名称和对象字段名称一致
	 * @return 返回封装完成的对象
	 */
	public static Object getObjectByMap(Class claz, Map<String, String> map) {
		Object obj = null;
		try {
			Field[] fields = claz.getDeclaredFields();
			obj = claz.newInstance();
			for (Field field : fields) {
				String strFieldName = field.getName();
				String value = map.get(strFieldName);
				Object objValue = null;
				if (value != null) {
					if (field.getType().equals(String.class)) {
						objValue = value;
					} else if (field.getType().equals(int.class)) {
						objValue = Integer.parseInt(value);
					} else if (field.getType().equals(long.class)) {
						objValue = Long.parseLong(value);
					}
					String strFirstLetter = strFieldName.substring(0, 1)
							.toUpperCase();
					String setMethodName = "set" + strFirstLetter
							+ strFieldName.substring(1);
					Method setMethod = claz.getMethod(setMethodName,
							objValue.getClass());
					setMethod.invoke(obj, objValue);
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * @description 将一个数组按顺序封装进一个对象中
	 * @param claz
	 *            字节码类型
	 * @param lines
	 *            数组
	 * @return 返回一个封装好之后的对象
	 */
	public static Object getObjectByArray(Class claz, String[] lines) {
		Object obj = null;
		if (claz != null && lines != null && lines.length != 0) {
			try {
				Field[] fields = claz.getDeclaredFields();
				obj = claz.newInstance();
				int count = lines.length > fields.length ? fields.length
						: lines.length;
				for (int i = 0; i < count; i++) {
					Field field = fields[i];// 得到字段
					String strFieldName = field.getName();
					String value = lines[i];

					Object objValue = null;
					if (StringUtils.isNotBlank(value)) {
						if (field.getType().equals(String.class)) {
							objValue = value;
						} else if (field.getType().equals(int.class)) {
							objValue = Integer.parseInt(value);
						} else if (field.getType().equals(long.class)) {
							objValue = Long.parseLong(value);
						}
						String strFirstLetter = strFieldName.substring(0, 1)
								.toUpperCase();
						String setMethodName = "set" + strFirstLetter
								+ strFieldName.substring(1);
						Method setMethod = claz.getMethod(setMethodName,
								objValue.getClass());
						setMethod.invoke(obj, objValue);
					}
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

}
