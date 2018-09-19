package com.vdata.ckexchange;

import com.vdata.ckexchange.utils.InitUtils;

/**
 * @description 类加载器初始化的驱动类
 * @author tzc
 *
 */
public class ClassLoaderDriver {

	/**
	 * @description 初始化资源池
	 * @param zkServer zookeeper的连接
	 * @param nodePath zookeeper上节点路径
	 * @return 初始化成功返回true
	 */
	public static boolean init(String zkServer,String nodePath){
		return InitUtils.init(zkServer, nodePath);
	}
}
