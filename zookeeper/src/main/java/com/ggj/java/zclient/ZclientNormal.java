package com.ggj.java.zclient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ZclientNormal {
	public static void main(String[] args) throws IOException {
		test();
	}
	
	/**
	 * @Description: zkClient主要做了两件事情。
	 * @see:一件是在session loss和session expire时自动创建新的ZooKeeper实例进行重连。
	 * @see:一件是将一次性watcher包装为持久watcher。
	 * @see:后者的具体做法是简单的在watcher回调中，重新读取数据的同时再注册相同的watcher实例。
	 */
	private static void test() {
		final ZkClient zc = new ZkClient("localhost:2181");
		// 创建根节点
		/* Persistent与Ephemeral 就是持久化保存到本地和不持久化的区别 ,不能再临时节点下面创建子节点 */
		
		zc.createPersistent("/testroot");
		// zc.createEphemeral("/testroot");
		// 创建子节点
		zc.create("/testroot/node1", "node1", CreateMode.EPHEMERAL);
		
		zc.createPersistent("/testroot/node2");
		zc.create("/testroot/node2/test", "node1", CreateMode.EPHEMERAL);
		List<String> children = zc.getChildren("/testroot");
		log.info("根节点下面的字节点个数" + children.size());
		
		// 获得子节点个数
		
		int chidrenNumbers = zc.countChildren("/testroot");
		log.info("子节点个数" + chidrenNumbers);
		
		zc.writeData("/testroot/node2/test", "给节点写数据");
		
		// 删除节点
		zc.delete("/testroot/node2/test");
		zc.delete("/testroot/node2");
		zc.delete("/testroot/node1");
		zc.delete("/testroot");
	}
}
