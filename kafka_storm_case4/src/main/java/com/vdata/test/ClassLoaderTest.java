package com.vdata.test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class ClassLoaderTest {

	@Test
	public void classTest() throws Exception{
		URL[] urls={new URL("file:C:/commonFiles/vdata/vdata.jar")};
		URLClassLoader urlLoader=new URLClassLoader(urls);
		Class claz=urlLoader.loadClass("com.vdata.test.Vdata");
		Object obj=claz.newInstance();
		Method method=claz.getMethod("print");
		method.invoke(obj);
		System.out.println("----------over---------------");
	}
	
	@Test
	public void pathtest(){
		URL url=ClassLoader.getSystemClassLoader().getResource("./");
		System.out.println(url);
		URL url1=ClassLoaderTest.class.getResource("../../");
		System.out.println(url1);
	}
	
	
	@Test
	public void pathtest1() throws Exception{
		//获取工程目录
		String path=System.getProperty("user.dir");
		System.out.println(path);
		String jarUrl="file:"+path+"/lib/vdata.jar";
		URL[] urls={new URL(jarUrl)};
		URLClassLoader loader=new URLClassLoader(urls);
		Class claz=loader.loadClass("com.vdata.test.Vdata");
		Object obj=claz.newInstance();
		Method method=claz.getMethod("print");
		method.invoke(obj);
	}
	
	@Test
	public void myclassloaderTest() throws Exception{
		MyClassLoader load=new MyClassLoader();
		Class claz=load.loadClass("com.vdata.test.Vdata");
		Object obj=claz.newInstance();
		Method method=claz.getMethod("print");
		method.invoke(obj);
	}
}
