package com.ggj.java.curator.leader.latch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * 除非有客户端挂掉重新触发选举
 * @author:gaoguangjin
 * @date 2016/7/27 16:06
 */
@Slf4j
public class ClientLeaderLatch implements Closeable {
    private final Long TASK_TIME=5000L;
    private final String name;
    @Getter
    private final LeaderLatch leaderLatch;
    private static Executor executor = Executors.newCachedThreadPool();

    public ClientLeaderLatch(String name, CuratorFramework client, String path) throws Exception {
        this.name=name;
        //leaderSelector 多个客户端监听同一个节点
        this.leaderLatch=new LeaderLatch(client, path, name);
        //启动
        leaderLatch.start();
        // 可以添加多个Listener，告知外界
        leaderLatch.addListener(getLatchListener(), executor);
    }

    @Override
    public void close() throws IOException {
        leaderLatch.close();
    }

    public LeaderLatchListener getLatchListener() {
        return new LeaderLatchListener() {
            @Override
            public void isLeader() {
                log.info(name+" =>  isLeader ");
                doJob();
            }

            @Override
            public void notLeader() {
                log.info(name+" =>relinquishing  notLeader");
            }
        };
    }

    private void doJob()  {
        log.info(name+" =>doJob() begin");
        try {
            Thread.sleep(TASK_TIME);
        } catch (InterruptedException e) {
            log.error("doJob execptione :",e);
        }
        log.info(name+" =>doJob() end");
    }
}
