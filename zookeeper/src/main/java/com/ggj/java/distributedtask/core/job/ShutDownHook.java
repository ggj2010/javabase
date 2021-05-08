package com.ggj.java.distributedtask.core.job;

import com.ggj.java.distributedtask.core.registerycenter.zookeeper.MasterSelector;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * hook
 *
 * @author gaoguangjin
 */
@Slf4j
public class ShutDownHook {
    static {
        shutDownHook();
    }

    public static void addShutDownHook() {
        //
    }

    private static void shutDownHook() {
        log.info("init shutdownhook");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    log.info("excute shutdown hook");
                    //1、停止选举等于不执行任何任务
                    MasterSelector.getInstance().close();
                    //2、wait all job run over
                    JobManager.waitAllJobExcute();
                    ZookeeperRegistryCenter.getInstance().removeTempNode();
                    //3、remove临时节点
                    ZookeeperRegistryCenter.getInstance().getClient();
                } catch (IOException e) {
                    log.error("close error", e);
                }

            }
        });
    }
}
