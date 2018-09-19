package com.vdata.ckexchange.classloader;

import java.util.Map;

import com.vdata.ckexchange.resource.ShareResource;

/**
 * @description 从zookeeper中提取类文件的类加载器
 * @author tzc
 *
 */
public class ZookeeperClassLoader extends ClassLoader {

	public ZookeeperClassLoader(){
		super();
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkCreateClassLoader();
		}
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] by=loadData(name);
		if(by!=null&&by.length!=0){
			return defineClass(name,by, 0, by.length);
		}else{
			return super.findClass(name);
		}
	}
	
	//获取对应的字节数组
	private byte[] loadData(String name){
		byte[] by=null;
		//从字节资源池中获取字节数组
		Map<String, byte[]> map=ShareResource.getMap();
		if(map!=null&&map.size()!=0)
			by=map.get(name);
		
		return by;
	}
}
