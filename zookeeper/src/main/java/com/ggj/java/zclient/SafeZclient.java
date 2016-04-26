package com.ggj.java.zclient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.util.*;

/**
 * @author:gaoguangjin
 * @Description:生成环境创建的zookeeper节点需要带有安全验证
 * @Email:335424093@qq.com
 * @Date 2016/4/20 13:38
 */
@Slf4j
public class SafeZclient {
	
	public static void main(String[] args) throws IOException {
		server();
		client();
		//查看zookeeper节点
//		ls();
	}

	private static void ls() {
		for (int i = 0; i <20 ; i++) {
			ZkClient zc = new ZkClient("127.0.0.1:2181");
			if(!zc.exists("/tt"))
			zc.create("/tt", "test", CreateMode.EPHEMERAL);
			log.info(zc.readData("/tt"));
		}
	}

	private static void client() {
		try {
			ZkClient zc = new ZkClient("localhost:2181");
			String userTwo = "gao:gao";
			zc.addAuthInfo("digest", userTwo.getBytes());

            //使用admin就可以创建
			// String userOne = "admin:admin";
			// zc.addAuthInfo("digest",userOne.getBytes());
			
			String data = zc.readData("/safe");
			log.info("只读用户 " + userTwo + "读取safe节点返回数据：" + data);
			
			zc.create("/safe2", "test", CreateMode.EPHEMERAL);
			log.info("只读用户创建节点safe2成功：" + zc.readData("/safe2"));
			
			zc.create("/safe/test", "zookeeper加密是针对节点加密的", CreateMode.EPHEMERAL);
			log.info("只读用户创建节点失败：" + zc.readData("/safe/test"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * server端对safe接点加密
	 */
	private static void server() {
		try {
			ZkClient zc = new ZkClient("localhost:2181");
			List<ACL> acls = getACL(zc);
			if (zc.exists("/safe"))
				zc.delete("/safe");
			zc.create("/safe", "safe", acls, CreateMode.PERSISTENT);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 授权访问
	 * Zookeeper对权限的控制是节点级别的，而且不继承，即对父节点设置权限，其子节点不继承父节点的权限
	 * Zookeeper提供了几种认证方式
	 * world：有个单一的ID，anyone，表示任何人。
	 * auth：不使用任何ID，表示任何通过验证的用户（是通过ZK验证的用户？连接到此ZK服务器的用户？）。
	 * digest：使用 用户名：密码 字符串生成MD5哈希值作为ACL标识符ID。权限的验证通过直接发送用户名密码字符串的方式完成，
	 * ip：使用客户端主机ip地址作为一个ACL标识符，ACL表达式是以 addr/bits 这种格式表示的。ZK服务器会将addr的前bits位与客户端地址的前bits位来进行匹配验证权限。
	 * @param zooKeeper
	 */
	private static List<ACL> getACL(ZkClient zooKeeper) throws Exception {
		// 配置两个用户admin有读写权限，gao有读的权限
		String userOne = "admin:admin";
		String userTwo = "gao:gao";
		// zooKeeper.addAuthInfo("digest",userOne.getBytes("UTF-8"));
		Id idOne = new Id("digest", DigestAuthenticationProvider.generateDigest(userOne));
		Id idTwo = new Id("digest", DigestAuthenticationProvider.generateDigest(userTwo));
		// 读
		ACL acl = new ACL(ZooDefs.Perms.ALL, idOne);
		// 写
		ACL aclRead = new ACL(ZooDefs.Perms.READ, idTwo);
		List<ACL> acls = Arrays.asList(acl, aclRead);
		return acls;
	}
	
}
