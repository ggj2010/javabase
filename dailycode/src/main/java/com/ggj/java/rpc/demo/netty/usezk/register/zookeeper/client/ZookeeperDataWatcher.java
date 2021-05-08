package com.ggj.java.rpc.demo.netty.usezk.register.zookeeper.client;

import com.ggj.java.qiniu.speciall.StringUtils;
import com.ggj.java.rpc.demo.netty.usezk.client.ClientProvider;
import com.ggj.java.rpc.demo.netty.usezk.util.CuratorUtil;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ggj.java.rpc.demo.netty.first.client.ClientProvider.getChannelFuture;

/**
 * 监听zk节点，下线时候变动
 *
 * @author gaoguangjin
 */
@Slf4j
public class ZookeeperDataWatcher implements CuratorListener {
    /**
     *  className -> ipList
     *  ip-> 多个链接 (删除机器时候 客户端可以自动发现并且删除链接)
     *  ip->classNameSet(增加机器时候 客户端可以自动发现并且添加链接)
     * @param client
     * @param event
     * @throws Exception
     */
    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        if (event.getType() == CuratorEventType.WATCHED && event.getPath() != null) {
            String path = event.getPath();
            String className = path.split("\\/")[2];
            String iplist = CuratorUtil.getStringDataAndWatch(client, path);
            if (StringUtils.isEmpty(iplist)) {
                return;
            }
            String[] iparrays = iplist.split("\\,");
            List<String> nowIpList = new ArrayList<>();
            for (String iparray : iparrays) {
                nowIpList.add(iparray);
            }

            List<String> oldIpList = ClientProvider.getClientConnectMap().get(className).getIpList();
            //以前这个class还没创建过请求
            if (CollectionUtils.isEmpty(oldIpList)) {
                return;
            }

            //判断扩容还是缩容
            if (nowIpList.size() > oldIpList.size()) {
                log.info("扩容,nowIplist={},oldIplist={}", nowIpList, oldIpList);
                nowIpList.removeAll(oldIpList);
                for (String nowIp : nowIpList) {
                    ClientProvider.initClient(nowIp);
                    ClientProvider.getClientConnectMap().get(className).getIpList().add(nowIp);
                }
            } else {
                log.info("锁容,nowIplist={},oldIplist={}", nowIpList, oldIpList);
                List<String> tempList = new ArrayList();
                tempList.addAll(oldIpList);
                tempList.removeAll(nowIpList);
                for (String oldIp : tempList) {
                    ClientProvider.getClientConnectMap().get(className).getIpList().remove(oldIp);
                    List<ChannelFuture> futureList = ClientProvider.getChannelFutureConnectMap().get(oldIp);
                    for (ChannelFuture channelFuture : futureList) {
                        channelFuture.channel().closeFuture();
                    }
                    ClientProvider.getChannelFutureConnectMap().remove(oldIp);
                }
            }
        }
    }
}
