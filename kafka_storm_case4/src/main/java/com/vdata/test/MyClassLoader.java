package com.vdata.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @description 自定义类加载器
 * @author tzc
 *
 */
public class MyClassLoader extends ClassLoader {

	public MyClassLoader() {
		super();
		SecurityManager security = System.getSecurityManager();
		if (security != null) {
			security.checkCreateClassLoader();
		}
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] by = null;
		try {
			by = getByte();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defineClass(name, by, 0, by.length);
	}

	// 获取一个字节数组
	private byte[] getByte() throws Exception {
		InputStream in = new FileInputStream(new File("classconf/Vdata.class"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] by = new byte[1024];
		int num = 0;
		while ((num = in.read(by)) != -1) {
			out.write(by, 0, num);
		}
		byte[] result = out.toByteArray();
		in.close();
		out.close();
		return result;
	}

}
