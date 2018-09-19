package com.vdata.ckexchange.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 资源池
 * @author tzc
 *
 */
public class ShareResource {
	//用于存放字节资源
	private static Map<String, byte[]> map=new HashMap<String, byte[]>();
	private static boolean updateFlag=true;//更新标识，第一次必须加装
	
	/**
	 * @description 通过class的全路径进行查找对应的字节数组
	 * @param clazName class全路径
	 * @return 返回一个字节数组
	 */
	public static byte[] findClassByte(String clazName){
		byte[] by=null;
		if(map!=null)
			by=map.get(clazName);
		return by;
	}
	
	
	public static void setMap(Map<String, byte[]> map) {
		ShareResource.map = map;
	}


	public static boolean isUpdateFlag() {
		return updateFlag;
	}


	public static void setUpdateFlag(boolean updateFlag) {
		ShareResource.updateFlag = updateFlag;
	}


	public static Map<String, byte[]> getMap() {
		return map;
	}
	
	
}
