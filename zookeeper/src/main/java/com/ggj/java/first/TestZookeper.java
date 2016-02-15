package com.ggj.java.first;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: TestZookeper.java
 * @Description:用来测试zookeper
 * @author gaoguangjin
 * @Date 2015-2-9 上午9:22:49
 */
@Slf4j
public class TestZookeper {
	// zookeper监听的地址和端口号
	public static String URL = "localhost";
	// 测试集群
	public static String CLIENT_POORT = "2181";
	
	// public static String CLIENT_POORT = "2182";
	
	public static void main(String[] args) {
		try {
			demo1();
		} catch (Exception e) {
			log.info("zookeper测试失败！:" + e.getLocalizedMessage());
		}
		
	}
	
	/**
	 * @Description:用来测试zookeper
	 * @return: void
	 * @throws IOException
	 */
	private static void demo1() throws Exception {
		// 第二个参数为Session超时时间，第三个为节点变化时
		ZooKeeper zk = new ZooKeeper(URL + ":" + CLIENT_POORT, 60000, new Watcher() {
			// 监控所有被触发的事件
			public void process(WatchedEvent event) {
				System.out.println("监控:" + event.getType());
			}
		});
		
		// test(zk);
		test1(zk);
		// jiqun(zk);
		
	}
	
	private static void jiqun(ZooKeeper zk) {
		// 测试集群
		// 1、 赋值2181zookeper
		// testjiqun(zk);
		
		// 2、 获取2182端口zookeper值
		// getjiqun(zk);
		
	}
	
	private static void getjiqun(ZooKeeper zk) throws KeeperException, InterruptedException {
		log.info(new String(zk.getData("/testRootPath", false, null)));
	}
	
	private static void testjiqun(ZooKeeper zk) throws KeeperException, InterruptedException {
		// 创建一个目录节点
		zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		log.info(new String(zk.getData("/testRootPath", false, null)));
	}
	
	private static void test(ZooKeeper zk) throws KeeperException, InterruptedException {
		// 创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
		zk.create("/root", "mydata".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// /在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
		zk.create("/root/childone", "childone".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		// 取得/root节点下的子节点名称,返回List<String>
		List<String> listStr = zk.getChildren("/root", true);
		for (String string : listStr)
			log.info(string);
		
		// 删除创建过的
		zk.delete("/root/childone", -1);
		zk.delete("/root", -1);
		
	}
	
	private static void test1(ZooKeeper zk) throws KeeperException, InterruptedException {
		// 创建一个目录节点
		zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// 创建一个子目录节点
		zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRootPath", false, null)));
		// 取出子目录节点列表
		System.out.println(zk.getChildren("/testRootPath", true));
		// 修改子目录节点数据
		zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
		System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");
		// 创建另外一个子目录节点
		zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo", true, null)));
		// 删除子目录节点
		zk.delete("/testRootPath/testChildPathTwo", -1);
		zk.delete("/testRootPath/testChildPathOne", -1);
		// 删除父目录节点
		zk.delete("/testRootPath", -1);
		// 关闭连接
		zk.close();
		
	}
}
