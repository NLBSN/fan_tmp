package com.fan.ckexchange;

import com.fan.ckexchange.utils.InitUtils;

/**
 * @author fan
 * @description 类加载器初始化的驱动类
 */
public class ClassLoaderDriver {

    /**
     * @param zkServer zookeeper的连接
     * @param nodePath zookeeper上节点路径
     * @return 初始化成功返回true
     * @description 初始化资源池
     */
    public static boolean init(String zkServer, String nodePath) {
        return InitUtils.init(zkServer, nodePath);
    }
}
