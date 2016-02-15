package com.ggj.java.zclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName:ZclientTest.java
 * @Description: zkclietApi测试
 * @author gaoguangjin
 * @Date 2015-7-2 上午10:07:23
 */
public class ZclientSub {
	public static void main(String[] args) throws IOException {
		test();
		System.in.read();
	}
	
	/**
	 * @Description: zkClient主要做了两件事情。
	 * @see:一件是在session loss和session expire时自动创建新的ZooKeeper实例进行重连。
	 * @see:一件是将一次性watcher包装为持久watcher。
	 * @see:后者的具体做法是简单的在watcher回调中，重新读取数据的同时再注册相同的watcher实例。
	 */
	private static void test() {
		final ZkClient zkClient4subChild = new ZkClient("localhost:2181");
		zkClient4subChild.subscribeChildChanges("/serverroot", new IZkChildListener() {
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println("=======test===");
				for (String string : currentChilds) {
					System.out.print(zkClient4subChild.readData("/serverroot/" + string, false) + ";");
				}
			}
		});
	}
}
