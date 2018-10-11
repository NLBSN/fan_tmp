package com.old_kafka_storm_cycle_case4.ckexchange.test;

import com.old_kafka_storm_cycle_case4.ckexchange.ClassLoaderDriver;
import com.old_kafka_storm_cycle_case4.ckexchange.classloader.ZookeeperClassLoader;
import com.old_kafka_storm_cycle_case4.ckexchange.outdata.SetDataToZkUtils;
import com.old_kafka_storm_cycle_case4.ckexchange.resource.ShareResource;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @description zk
 */
public class ZkClassLoaderTest {
	private String zkServer="vdata1:2181,vdata2:2181,vdata3:2181";
	private String nodePath="/vdata";
	
	@Test
	public void test() throws Exception{
		boolean initFlag=ClassLoaderDriver.init(zkServer, nodePath);
		System.out.println("------init------"+initFlag);
		
		ZookeeperClassLoader classLoader=null;
		while(true){
			if(ShareResource.isUpdateFlag()){
				classLoader=new ZookeeperClassLoader();
				ShareResource.setUpdateFlag(false);
			}
			
			Class claz=classLoader.loadClass("com.old_kafka_storm_cycle_case4.test.Vdata");
			Object obj=claz.newInstance();
			Method method=claz.getMethod("print");
			method.invoke(obj);
			System.out.println("----------over---------------");
			Thread.sleep(8000);
		}
	}
	
	@Test
	public void setDataTest(){
		SetDataToZkUtils.setDataToZk("com", zkServer, nodePath);
		System.out.println("--------over loader-------------");
	}
}
