package com.ggj.java.zclient;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;

/**
 * @ClassName:ZclientTest.java
 * @Description: zkclietApi测试
 * @author gaoguangjin
 * @Date 2015-7-2 上午10:07:23
 */
public class ZclientServerPub {
	public static void main(String[] args) throws IOException, InterruptedException {
		test();
	}
	
	private static void test() throws InterruptedException {
		final ZkClient zkClient4subChild = new ZkClient("localhost:2181");
		
		zkClient4subChild.create("/serverroot", "serverroot", Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		Thread.sleep(4000);
		zkClient4subChild.create("/serverroot/server1", "server1", Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		Thread.sleep(4000);
		zkClient4subChild.create("/serverroot/server2", "server2", Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		Thread.sleep(20000);
		zkClient4subChild.delete("/serverroot/server1");
		zkClient4subChild.delete("/serverroot/server2");
		zkClient4subChild.delete("/serverroot");
	}
}
