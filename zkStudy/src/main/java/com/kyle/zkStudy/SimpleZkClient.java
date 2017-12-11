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

	// zk集群链接串
	private static final String connectString = "kyle001:2181,kyle002:2181,kyle003:2181";
	// timeout时间
	private static final int sessionTimeout = 2000;

	ZooKeeper zkClient = null;

	public void init() throws Exception {
		// 创建客户端对象
		zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				// 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
				System.out.println(event.getType() + "---" + event.getPath());
				try {
					// 对根目录进行监听，由于监听事件触发一次即完成，所以要不断重新监听
					zkClient.getChildren("/", true);
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * 创建节点
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void createZnode() throws KeeperException, InterruptedException {
		// 参数1：要创建的节点的路径 
		// 参数2：节点数据  数据类型是byte类型，也就是说一个配置文件转换成byte也可以保存
		// 参数3：节点的权限  Ids类的
		// 参数4：节点的类型 枚举类型，对应四种类别的节点
		// 上传的数据可以是任何类型，但都要转成byte[]
		String nodeCreated = zkClient.create("/JAVAClient", "helloZK".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		System.out.println("创建节点成功" + nodeCreated);
	}

	/**
	 * 判断节点是否存在
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void checkZnode() throws KeeperException, InterruptedException {
		// 参数1：节点的路径 
		// 参数2：是否对节点进行监听
		Stat stat = zkClient.exists("/JAVAClient", false);
		System.out.println(stat == null ? "not exist" : "exist");
	}

	/**
	 * 获取子节点
	 * @throws Exception
	 */
	public void getChildren() throws Exception {
		// 参数1：节点的路径 
		// 参数2：是否对节点进行监听
		List<String> children = zkClient.getChildren("/", true);
		for (String child : children) {
			System.out.println(child);
		}
	}

	/**
	 * 获取节点数据
	 * @throws Exception
	 */
	public void getData() throws Exception {
		// 参数1：节点路径
		// 参数2：是否 对节点监听
		// 参数3：节点状态（方便定位获取版本）
		byte[] data = zkClient.getData("/JAVAClient", false, null);
		System.out.println(new String(data));

	}

	/**
	 * 删除节点
	 * @throws Exception
	 */
	public void deleteZnode() throws Exception {

		// 参数1：阶段路径
		// 参数2：指定要删除的版本，-1表示删除所有版本
		// Delete the node with the given path. 
		// The call will succeed if such a node exists, 
		// and the given version matches the node's version
		// (if the given version is -1, it matches any node's versions). 
		zkClient.delete("/JAVAClient", -1);

	}

	/**
	 * 修改节点内容
	 * @throws Exception
	 */
	public void setData() throws Exception {

		// 参数1：阶段路径
		// 参数2：修改的内容
		// 参数3：指定要修改的版本，-1表示删除所有版本
		Stat setData = zkClient.setData("/JAVAClient", "helloZookeeper".getBytes(), -1);
		System.out.println(setData.toString());
		byte[] data = zkClient.getData("/JAVAClient", false, null);
		System.out.println(new String(data));

	}

}
