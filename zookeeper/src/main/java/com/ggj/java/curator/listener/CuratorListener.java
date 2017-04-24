package com.ggj.java.curator.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

import com.ggj.java.curator.CuratorUtil;
import org.apache.zookeeper.CreateMode;

import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_ADDED;
import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_REMOVED;

/**
 * Curator提供了三种Watcher(Cache)来监听结点的变化.
 * Path Cache：监视一个路径下1）孩子结点的创建、2）删除，3）以及结点数据的更新。产生的事件会传递给注册的PathChildrenCacheListener。
 Node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地。
 Tree Cache：Path Cache和Node Cache的“合体”，监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据
 * @author:gaoguangjin
 * @date 2016/8/2 10:54
 */
@Slf4j
public class CuratorListener {
	
	private static final String LISTERNER_PATH = "/root/listener";
	
	private static final String LISTERNER_CHILD_PATH = "/child1";
	private static final String LISTERNER_CHILD_TWO_PATH = "/child3";
	private static final String LISTERNER_DISCONNECT_PATH = "/root/disconnect";
	/**
	 * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
	 */
	static ExecutorService pool = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorUtil.getClient();
		//demo1(client);
//		demo2(client);
//		demo3(client);
		//测试事件 CHILD_REMOVED,先用demo4  再用demo5 再用demo6
//		demo4(client);
		//如果不是显示的去删除这个零时节点，那么主动退出程序默认是三十秒 这个节点才会被删除
//		demo5(client);
		demo6(client);
		System.in.read();
	}

	private static void demo6(CuratorFramework client) {
		try {
			client.delete().forPath(LISTERNER_DISCONNECT_PATH+LISTERNER_CHILD_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void demo5(CuratorFramework client) throws Exception {
		if(!CuratorUtil.checkExists(client,LISTERNER_DISCONNECT_PATH+LISTERNER_CHILD_PATH))
			client.create().withMode(CreateMode.EPHEMERAL).forPath(LISTERNER_DISCONNECT_PATH+LISTERNER_CHILD_PATH, "child2".getBytes());
	}

	private static void demo4(CuratorFramework client) throws Exception {
		if(!CuratorUtil.checkExists(client,LISTERNER_DISCONNECT_PATH))
			client.create().creatingParentsIfNeeded().forPath(LISTERNER_DISCONNECT_PATH, "hello".getBytes());
		/**
		 * 监听子节点的变化情况
		 */
		final PathChildrenCache childrenCache = new PathChildrenCache(client, LISTERNER_DISCONNECT_PATH, true);
		childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
		childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch(event.getType()) {
					case CHILD_ADDED:
						log.info("CHILD_ADDED: " + event.getData().getPath());
						break;
					case CHILD_REMOVED:
						log.info("CHILD_REMOVED: " + event.getData().getPath());
						break;
					case CHILD_UPDATED:
						log.info("CHILD_UPDATED: " + event.getData().getPath());
						break;
					default:
						log.info("default");
						break;
				}
			}
		});
	}

	private static void demo3(CuratorFramework client) throws Exception {
		log.info("demo3");
		if(!CuratorUtil.checkExists(client,LISTERNER_PATH+LISTERNER_CHILD_TWO_PATH))
			client.create().withMode(CreateMode.EPHEMERAL).forPath(LISTERNER_PATH+LISTERNER_CHILD_TWO_PATH, "child2".getBytes());
//		client.delete().forPath(LISTERNER_PATH+LISTERNER_CHILD_TWO_PATH);
	}

	private static void demo2(CuratorFramework client) throws Exception {
		if(!CuratorUtil.checkExists(client,LISTERNER_PATH))
			client.create().creatingParentsIfNeeded().forPath(LISTERNER_PATH, "hello".getBytes());
		/**
		 * 监听子节点的变化情况
		 */
		final PathChildrenCache childrenCache = new PathChildrenCache(client, LISTERNER_PATH, true);
		childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
		childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch(event.getType()) {
					case CHILD_ADDED:
						log.info("CHILD_ADDED: " + event.getData().getPath());
						break;
					case CHILD_REMOVED:
						log.info("CHILD_REMOVED: " + event.getData().getPath());
						break;
					case CHILD_UPDATED:
						log.info("CHILD_UPDATED: " + event.getData().getPath());
						break;
					default:
						log.info("default");
						break;
				}
			}
		});
		if(!CuratorUtil.checkExists(client,LISTERNER_PATH+LISTERNER_CHILD_PATH))
			client.create().forPath(LISTERNER_PATH+LISTERNER_CHILD_PATH, "child1".getBytes());
	}

	private static void demo1(CuratorFramework client) throws Exception {

		if(!CuratorUtil.checkExists(client,LISTERNER_PATH))
			client.create().creatingParentsIfNeeded().forPath(LISTERNER_PATH, "hello".getBytes());
		/**
		 * 监听数据节点的变化情况
		 */
		final NodeCache nodeCache = new NodeCache(client, LISTERNER_PATH, false);
		nodeCache.start(true);
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				log.info("Node data is changed, new data: " + new String(nodeCache.getCurrentData().getData()));
			}
		}, pool);


		/**
		 * 监听子节点的变化情况
		 */
		final PathChildrenCache childrenCache = new PathChildrenCache(client, LISTERNER_PATH, true);
		childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
		childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch(event.getType()) {
					case CHILD_ADDED:
						log.info("CHILD_ADDED: " + event.getData().getPath());
						break;
					case CHILD_REMOVED:
						log.info("CHILD_REMOVED: " + event.getData().getPath());
						break;
					case CHILD_UPDATED:
						log.info("CHILD_UPDATED: " + event.getData().getPath());
						break;
					default:
						break;
				}
			}
		}, pool);
		client.setData().forPath(LISTERNER_PATH, "world".getBytes());
		if(!CuratorUtil.checkExists(client,LISTERNER_PATH+LISTERNER_CHILD_PATH))
			client.create().forPath(LISTERNER_PATH+LISTERNER_CHILD_PATH, "child".getBytes());
		Thread.sleep(10 * 1000);
		pool.shutdown();
		client.close();
	}
}
