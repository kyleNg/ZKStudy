package com.kyle.zkStudy;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class SimpleZkClient {
	
	private static final String connectString = "192.168.0.110:2181,192.168.0.111:2181,192.168.0.112:2181";
	private static final int sessionTimeout = 2000;
	
	ZooKeeper zkClient = null;
	public void init() throws Exception{
		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher(){
			public void process(WatchedEvent event) {
				// 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
				System.out.println(event.getType() + "---" + event.getPath());
				try {
					zkClient.getChildren("/", true);
				} catch (Exception e) {
				}	
			}
			
		});
	}
	// 创建数据节点到zk中
	public void createZnode() throws KeeperException, InterruptedException{
		// 参数1：要创建的节点的路径 参数2：节点大数据 参数3：节点的权限 参数4：节点的类型
		// 上传的数据可以是任何类型，但都要转成byte[]
		String nodeCreated = zkClient.create("/JAVAClint", "helloZK".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("创建节点成功"+nodeCreated);
	}
	
	// 判断znode是否存在
	public void checkZnode() throws KeeperException, InterruptedException{
		Stat stat = zkClient.exists("/JAVAClint", false);
		System.out.println(stat==null?"not exist":"exist");
	}
	
	// 获取子节点
	public void getChildren() throws Exception {
		List<String> children = zkClient.getChildren("/", true);
		for (String child : children) {
			System.out.println(child);
		}
	}
	
	// 获取节点数据
	public void getData() throws Exception {
		
		byte[] data = zkClient.getData("/JAVAClint", false, null);
		System.out.println(new String(data));
		
	}
	
	//删除znode
	public void deleteZnode() throws Exception {
		
		//参数2：指定要删除的版本，-1表示删除所有版本
		zkClient.delete("/JAVAClint", -1);
		
	}
	
	//修改znode
	public void setData() throws Exception {
		
		zkClient.setData("/JAVAClint", "imissyou angelababy".getBytes(), -1);
		
		byte[] data = zkClient.getData("/JAVAClint", false, null);
		System.out.println(new String(data));
		
	}
	
	
}
