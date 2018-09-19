package com.vdata.ckexchange.outdata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @description 负责写入数据到zookeeper指定的目录下
 * @author tzc
 *
 */
public class SetDataToZkUtils {

	/**
	 * @description 将class文件写入到zookeeper上
	 * @param clazPath class文件所在的目录名称
	 * @param zkServer zk的连接
	 * @param zkPath 数据存放在zookeeper上面的目录
	 * @return 如果存放成功，返回true
	 */
	public static boolean setDataToZk(String clazPath,String zkServer,String zkPath){
		boolean flag=false;
		//获取zookeeper的连接点
		if(!zkPath.contains("vdata"))
			throw new RuntimeException("-----the path must contains vdata------------");
		
		ZooKeeper zk=getZkConnection(zkServer);
		try {
			Map<String, byte[]> clazMap=getData(clazPath);
			if(clazMap!=null&&clazMap.size()!=0){
				Set<Entry<String, byte[]>> clazSet=clazMap.entrySet();
				for(Entry<String, byte[]> entry:clazSet){
					String tmpKey=entry.getKey();
					//组合zk上面的路径
					String tmpZkPath=zkPath+"/"+tmpKey;
					//首先判断子节点是否存在，如果不存在则需要进行创建
					Stat stat=zk.exists(tmpZkPath, false);
					if(stat==null)
						zk.create(tmpZkPath, "create".getBytes(),
									Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					byte[] dataByte=entry.getValue();
					
					int version=zk.exists(tmpZkPath, false).getVersion();
					zk.setData(tmpZkPath, dataByte, version);
					System.out.println(tmpKey+"-------version---------"+version);
				}
				
				//获取根目录的的版本，进行一次数据更新，以便于后面只需要针对根目录
				//定义一个观察者即可
				int tmpVersion=zk.exists(zkPath, false).getVersion();
				zk.setData(zkPath, "update".getBytes(), tmpVersion);
				
				flag=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(zk!=null)
					zk.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return flag;
	}
	
	
	
	//获取zookeeper的连接点
	private static ZooKeeper getZkConnection(String zkServer){
		ZooKeeper client=null;
		try {
			client = new ZooKeeper(zkServer, 5000, new Watcher(){

				@Override
				public void process(WatchedEvent event) {
					System.out.println("----------init----------------");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return client;
	}
	
	//将目录文件读取出来，并以字节数组方式存放
	private static Map<String, byte[]> getData(String filePath){
		Map<String, byte[]> classByteMap=new HashMap<String, byte[]>();
		List<File> fileList=new ArrayList<File>();
		//递归读取所有文件路径
		readFileOfAll(new File(filePath), fileList);
		
		//根据文件路径读取所有的文件
		for(File tmpFile:fileList){
			String tmpFilePath=tmpFile.getPath();
			String tmpKey=tmpFilePath.replace("\\", ".")
									.replace(".class", "");
			InputStream in=null;
			ByteArrayOutputStream out=null;
			try {
				in=new FileInputStream(tmpFile);
				out=new ByteArrayOutputStream();
				byte[] by=new byte[1024];
				int num=0;
				while((num=in.read(by))!=-1){
					out.write(by, 0, num);
				}
				
				//获取字节数组之后存放到map中
				byte[] classByte=out.toByteArray();
				if(classByte!=null&&classByte.length!=0){
					classByteMap.put(tmpKey, classByte);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(in!=null)
						in.close();
					if(out!=null)
						out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		return classByteMap;
	}
	
	//循环递归文件
	private static void readFileOfAll(File file,List<File> list){
		if(file.isDirectory()){
			File[] listFile=file.listFiles();
			for(File tmpFile:listFile){
				if(tmpFile.isDirectory()){//如果是目录，继续进行递归
					readFileOfAll(tmpFile, list);
				}else{//如果是文件，将其添加到list中
					list.add(tmpFile);
				}
			}
		}else{
			list.add(file);
		}
	}
	
}
