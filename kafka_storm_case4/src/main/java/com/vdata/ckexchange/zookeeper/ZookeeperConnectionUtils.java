package com.vdata.ckexchange.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * @description 获取zookeeper的连接点
 * @author tzc
 *
 */
public class ZookeeperConnectionUtils {
	private static final Logger LOG=Logger.getLogger(ZookeeperConnectionUtils.class);
	//zookeeper的连接点
	private static ZooKeeper zk=null;
	
	/**
	 * @description 获取zookeeper的连接点，此处加了同步，防止线程并发异常
	 * @param zkServer zk的连接
	 * @return 返回一个连接点
	 */
	public static synchronized ZooKeeper getZkConnection(String zkServer){
		if(zk==null){
			try {
				zk=new ZooKeeper(zkServer, 5000, new Watcher(){

					@Override
					public void process(WatchedEvent event) {
						if(event.getType().equals(EventType.None)){
							if(event.getState().equals(KeeperState.SyncConnected)){
								LOG.info("-------------------"+KeeperState.SyncConnected);
							}else if(event.getState().equals(KeeperState.Expired)){
								zk=null;
								LOG.error("---------session expried----------------");
							}
						}
					}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return zk;
	}
	
	/**
	 * @description 判断zk连接点是否超时
	 * @return 返回false说明超时
	 */
	public static boolean isAlive(){
		boolean flag=false;
		if(zk!=null)
			flag=zk.getState().isAlive();
		
		return flag;
	}
	
}
