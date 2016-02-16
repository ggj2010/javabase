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
