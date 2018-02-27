package com.ggj.java.curator.leader;

import com.ggj.java.curator.CuratorUtil;
import com.ggj.java.curator.leader.latch.ClientLeaderLatch;
import com.ggj.java.curator.leader.selector.ClientSelector;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 *
 * 选举主要依赖于LeaderSelector和LeaderLatch2个类。前者是所有存活的客户端不间断的轮流做Leader 后者是一旦选举出Leader，除非有客户端挂掉重新触发选举，
 * @author:gaoguangjin
 * @date 2016/7/27 15:08
 */
@Slf4j
public class LeaderDemo {
    private static final String LEADER_PATH = "/root/leader";
    private static final String LEADER_LATCH_PATH = "/root/leaderlatch";
    private static final int CLIENT_SIZE =2;
    private static CopyOnWriteArrayList<LeaderLatch> list=new CopyOnWriteArrayList();

    /**
     * Curator的封装 api使用起来简单易懂
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //1、测试LeaderSelector 轮训
        testSelector();
        //1、测试LeaderLatch 有客户端退出再重新触发选举
//        testLeaderLatch();
        System.in.read();
    }

    private static void testLeaderLatch() {
        //第一启动 leaderLatch(1);方法，leaderLatch（2）方法注释,第二次启动  leaderLatch(2)方法，leaderLatch(1)注释
//          leaderLatch(1,2);
//          leaderLatch(2,2);
          leaderLatch(3,2);
    }

    private static void leaderLatch(int number,int type) {
        ExecutorService pool= Executors.newFixedThreadPool(CLIENT_SIZE);
        //自定义5个client 节点
        for(int i=1;i<=CLIENT_SIZE;i++){
            pool.execute(getCuratorFrameworkThread(i*number,type));
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            while(true) {
                for (LeaderLatch leaderLatch : list) {
                    if (leaderLatch.hasLeadership()) {
                        log.info("current leaderLatch is :"+leaderLatch.getId());
                        break;
                    }
                }
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            log.error("leaderLatch exception",e);
        }

    }

    private static void testSelector() {
        //第一启动 selector(1);方法，将selector（2）方法注释,第二次启动  elector(2)方法，将selector(1)注释
        selector(1,1);
//        selector(2,1);
    }

    private static void selector(int number,int type) {
        ExecutorService pool= Executors.newFixedThreadPool(CLIENT_SIZE);
        //自定义5个client 节点
        for(int i=1;i<=CLIENT_SIZE;i++){
            pool.execute(getCuratorFrameworkThread(i*number,type));
        }
        pool.shutdown();
    }

    private static Runnable getCuratorFrameworkThread(final int number,final int type) {
       return new Thread(()->{
           CuratorFramework client = CuratorUtil.getClient();
           if(type==1) {
              new ClientSelector("Selector客户端：" + number, client, LEADER_PATH);
           }else{
               try {
                   ClientLeaderLatch clientLeaderLatch= new ClientLeaderLatch("LeaderLatch客户端：" + number, client, LEADER_LATCH_PATH);
                   list.add(clientLeaderLatch.getLeaderLatch());
               } catch (Exception e) {
                   log.error("create ClientLeaderLatch exception",e);
               }
           }
        });
    }
}
