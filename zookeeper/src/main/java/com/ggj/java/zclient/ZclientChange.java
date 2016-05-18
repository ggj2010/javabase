package com.ggj.java.zclient;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * @author:gaoguangjin
 * @date 2016/5/6 13:28
 */
@Slf4j
public class ZclientChange {
    public static void main(String[] args) throws IOException {
        test();

        System.in.read();
    }
    private static void test() {
        final ZkClient zkClient = new ZkClient("localhost:2181");

        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                //当zookeeper被关闭时候 会触发这个事件
                if (state == Watcher.Event.KeeperState.Disconnected) {
                  log.info("Disconnected");
                } else if (state == Watcher.Event.KeeperState.SyncConnected) {
                    //  当zookeeper被重新连接的时候 会触发这个事件
                    log.info("SyncConnected");
                }
            }
            @Override
            public void handleNewSession() throws Exception {
                log.info("handleNewSession");
            }

            @Override
            public void handleSessionEstablishmentError(Throwable error) throws Exception {
                log.info("handleSessionEstablishmentError");
            }
        });
    }

}
