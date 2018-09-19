package com.vdata.ckexchange.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.vdata.ckexchange.resource.ShareResource;
import com.vdata.ckexchange.zookeeper.ZookeeperConnectionUtils;
import com.vdata.ckexchange.zookeeper.watcher.ClassloaderWatcher;

/**
 * @description 用于和zookeeper进行交互，加载zookeeper中的数据
 * @author tzc
 *
 */
public class InitUtils {
	private static final Logger LOG=Logger.getLogger(InitUtils.class);

	/**
	 * @description 加载zookeeper中数据
	 * @param zkServer zookeeper的连接
	 * @param nodePath zookeeper上的节点路径
	 * @return 加载成功返回true
	 */
	public static boolean init(String zkServer,String nodePath){
		boolean flag=false;
		ZooKeeper zk=ZookeeperConnectionUtils.getZkConnection(zkServer);
		
		Watcher watcher=new ClassloaderWatcher(zkServer, nodePath);
		try {
			zk.exists(nodePath, watcher);
			List<String> childrenList=zk.getChildren(nodePath, null);
			if(childrenList!=null&&childrenList.size()!=0){
				Map<String, byte[]> map=new HashMap<String, byte[]>();
				
				for(String tmpPath:childrenList){//循环从zookeeper中读取数据
					
					byte[] by=zk.getData(nodePath+"/"+tmpPath, false, null);
					if(by!=null&&by.length!=0)
						map.put(tmpPath, by);
				}
				
				if(map!=null&&map.size()!=0){//更新资源池中的map和标识
					ShareResource.setMap(map);
					ShareResource.setUpdateFlag(true);
					flag=true;
				}
			}
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		} 
		
		return flag;
	}
}
