package com.ggj.java.curator.leader.selector;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:gaoguangjin
 * @date 2016/7/27 15:17
 */
@Slf4j
public class ClientSelector extends LeaderSelectorListenerAdapter implements Closeable {
    private final Long TASK_TIME=5000L;
    private final String name;
    private final LeaderSelector leaderSelector;
    private final AtomicInteger leaderCount = new AtomicInteger();
    public ClientSelector(String name,CuratorFramework client, String path) {
        this.name=name;
        //leaderSelector 多个客户端监听同一个节点
        this.leaderSelector=new LeaderSelector(client, path, this);
        //保证在此实例释放领导权之后还可能获得领导权
        leaderSelector.autoRequeue();
        //启动
        leaderSelector.start();
    }

    @Override
    public void close() throws IOException {
        log.info(name+"close()");
        leaderSelector.close();
    }

    /**
     * 实现接口方法，当takeLeadership方法执行完之后 就会释放权限
     * @param curatorFramework
     * @throws Exception
     */
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        log.info(name+" =>takeLeadership()");
        log.info(name+" =>has been leader times:"+leaderCount.incrementAndGet());
        try{
            doJob();
        }catch (Exception e){
            log.error("takeLeadership exception:",e);
        }finally {
            log.info(name+" =>relinquishing  ");
        }
    }

    private void doJob() throws InterruptedException {
        log.info(name+" =>doJob() begin");
        Thread.sleep(TASK_TIME);
        log.info(name+" =>doJob() end");
    }
}
