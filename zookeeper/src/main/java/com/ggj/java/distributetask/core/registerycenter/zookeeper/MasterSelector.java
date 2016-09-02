package com.ggj.java.distributetask.core.registerycenter.zookeeper;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/7/27 15:17
 */
@Slf4j
public class MasterSelector extends LeaderSelectorListenerAdapter implements Closeable {
    private volatile static boolean isMaster=false;
    private final Long MASTER_TIME=1000L*60*1;
    private final String name;
    private final LeaderSelector leaderSelector;
    private final AtomicInteger leaderCount = new AtomicInteger();
    public MasterSelector(String name, CuratorFramework client, String path) {
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
        log.info(name+" =>has been leader times:"+leaderCount.incrementAndGet());
        try{
            isMaster=true;
            Thread.sleep(MASTER_TIME);
            isMaster=false;
        }catch (Exception e){
            log.error("takeLeadership exception:",e);
        }finally {
            log.info(name+" =>relinquishing  ");
        }
    }

    public static boolean isMaster() {
        return isMaster;
    }

}
