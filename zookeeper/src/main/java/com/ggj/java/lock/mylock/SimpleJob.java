package com.ggj.java.lock.mylock;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.util.*;

/**
 * 只执行一次，谁先拿到锁谁执行
 *
 * 获取锁实现思路：
 1.     首先创建一个作为锁目录(znode)，通常用它来描述锁定的实体，称为:/lock_node
 2.     希望获得锁的客户端在锁目录下创建znode，作为锁/lock_node的子节点，并且节点类型为有序临时节点(EPHEMERAL_SEQUENTIAL)；
 例如：有两个客户端创建znode，分别为/lock_node/lock-1和/lock_node/lock-2
 3.     当前客户端调用getChildren（/lock_node）得到锁目录所有子节点，不设置watch，接着获取小于自己(步骤2创建)的兄弟节点
 4.     步骤3中获取小于自己的节点不存在 && 最小节点与步骤2中创建的相同，说明当前客户端顺序号最小，获得锁，结束。
 5.     客户端监视(watch)相对自己次小的有序临时节点状态
 6.     如果监视的次小节点状态发生变化，则跳转到步骤3，继续后续操作，直到退出锁竞争。
 *
 *
 */
@Slf4j
public class SimpleJob implements Job {
    ZkClient zkClient = new ZkClient("localhost:2181");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (getLock()) {
            log.info("------------执行定时任务------");
        }
        ;
    }

    private boolean getLock() {
        if (!zkClient.exists("/lock"))
            zkClient.create("/lock", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //子节点名称/lock/child-0000000000
        String createdPath = zkClient.create("/lock/child-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("子节点名称：" + createdPath);

        //检查是否是最小的节点
        return checkIsMin(createdPath);
    }

    private boolean checkIsMin(String createdPath) {
        //获取当前节点的number序号
        String[] str = createdPath.split("-");
        Long number = Long.parseLong(str[1]);

        log.info("number=====" + number);

        List<Long> idList = new ArrayList<Long>();
        List<String> pathList = zkClient.getChildren("/lock");

        for (String s : pathList) {
            String[] str2 = s.split("-");
            long id = Long.parseLong(str2[1]);
            idList.add(id);
            log.info("id="+id);
        }

        Collections.sort(idList);

       //最后一个和当前比较 如果相等就执行这一个
        if (idList.get(0) == number) return true;

        return false;
    }


}
