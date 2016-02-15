package com.ggj.java.otherpeople;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.util.List;

@Slf4j
public class ZookeeperClient {
	static ZooKeeper zk = null;
	
	public static void main(String[] args) throws Exception {
		test();
		System.in.read();
		// delete();
	}
	
	private static void test() {
		try {
			Watcher wa = new Watcher() {
				// 监控所有被触发的事件
				public void process(WatchedEvent event) {
					System.out.println("====change===" + event.getType() + ":" + event.getPath());
					try {
						updateServerList();
					} catch (Exception e) {
						log.info("dd" + e.getLocalizedMessage());
					}
					log.info("监控:" + event.getType() + ":" + event.getPath());
				}
			};
			;
			// 第二个参数为Session超时时间，第三个为节点变化时
			zk = new ZooKeeper("localhost:2181", 60000, wa);
		} catch (Exception e) {
			log.error("错误！" + e.getLocalizedMessage());
		}
	}
	
	protected static void updateServerList() throws KeeperException, InterruptedException {
		//监控/serverroot
		List<String> subList = zk.getChildren("/serverroot", true);
		System.out.println("打印listserver");
		for (String string : subList) {
			System.out.print(new String(zk.getData("/serverroot" + "/" + string, true, null)) + ";");
		}
		System.out.println();
	}
	
	private static void addServerByName(String name) {
		try {
			zk.create("/serverroot/" + name, name.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (Exception e) {
			log.error("添加服务节点错误" + e.getLocalizedMessage());
		}
	}
	
	private static void delete() throws KeeperException, InterruptedException {
		List<String> subList = zk.getChildren("/serverroot", false);
		for (String string : subList) {
			zk.delete("/serverroot/" + string, -1);
		}
		zk.delete("/serverroot", -1);
	}
}
