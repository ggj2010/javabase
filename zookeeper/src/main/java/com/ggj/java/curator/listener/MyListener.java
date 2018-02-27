package com.ggj.java.curator.listener;

import com.ggj.java.curator.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import java.util.Date;

/**
 * @author:gaoguangjin
 * @date:2017/10/25
 */
@Slf4j
public class MyListener {
    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws Exception {
        CuratorFramework client = CuratorUtil.getNoStartClient();
        String path = "/a";

        CuratorListener listener = new CuratorListener() {
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                log.info("监听事件触发，event内容为：" + event);
                log.info("获取值{}，重新监听",new String(client.getData().watched().forPath(path)));
            }
        };
        client.getCuratorListenable().addListener(listener);
        client.start();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!CuratorUtil.checkExists(client, path)) {
                            client.create().forPath(path, "value".getBytes());
                        } else {
                            String value=new Date().getTime() + "";
                            log.info("设置值{}",value);
                            client.setData().forPath(path, value.getBytes());
                        }
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        System.in.read();
    }
}
