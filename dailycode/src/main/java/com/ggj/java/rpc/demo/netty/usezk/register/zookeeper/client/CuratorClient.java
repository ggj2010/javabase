package com.ggj.java.rpc.demo.netty.usezk.register.zookeeper.client;

import com.ggj.java.qiniu.speciall.StringUtils;
import com.ggj.java.rpc.demo.netty.usezk.util.Constants;
import com.ggj.java.rpc.demo.netty.usezk.util.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoguangjin
 */
@Slf4j
public class CuratorClient {

    private static CuratorFramework client = null;

    public CuratorClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(Constants.DEFAULT_CONNECT_URL, retryPolicy);
        client = CuratorFrameworkFactory.builder().namespace(Constants.NAME_SPACE).retryPolicy(retryPolicy).connectString(Constants.DEFAULT_CONNECT_URL).build();
        client.start();
    }

    private void setStringValue() {
    }

    /**
     * 并发注册会有问题
     *
     * @param zkPathConfig
     * @param serviceAddress
     * @throws Exception
     */
    public void registerService(ZKPathConfig zkPathConfig, String serviceAddress) throws Exception {
        try {
            if (CuratorUtil.checkExists(client, zkPathConfig.getServicePath())) {
                String resulst = CuratorUtil.getStringData(client, zkPathConfig.getServicePath());
                if (StringUtils.isEmpty(resulst)) {
                    CuratorUtil.update(client, zkPathConfig.getServicePath(), serviceAddress);
                } else {
                    String[] adressArray = resulst.split("\\,");
                    List<String> adressList = new ArrayList();
                    for (String registeredAdress : adressArray) {
                        //已注册就不需要重复注册
                        if (!registeredAdress.equals(serviceAddress)) {
                            adressList.add(registeredAdress.trim());
                        }
                    }
                    adressList.add(serviceAddress);
                    CuratorUtil.update(client, zkPathConfig.getServicePath(), org.apache.commons.lang.StringUtils.join(adressList, ","));
                }
            } else {
                CuratorUtil.create(client, zkPathConfig.getServicePath(), serviceAddress);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 注册应用信息
     *
     * @param zkPathConfig
     * @throws Exception
     */
    public void registerServerApp(ZKPathConfig zkPathConfig) throws Exception {
        CuratorUtil.create(client, zkPathConfig.getAppPath(), zkPathConfig.getServiceAddress(), true);

    }

    /**
     * 注册权重
     *
     * @param zkPathConfig
     * @throws Exception
     */
    public void registerWeight(ZKPathConfig zkPathConfig) throws Exception {
        CuratorUtil.create(client, zkPathConfig.getWeightPath(), String.valueOf(Constants.DEFAULT_ONLINE_WEIGHT), true);
    }

    /**
     * 下线服务机器
     *
     * @param zkPathConfig
     * @param serviceAddress
     * @throws Exception
     */
    public void unregisterService(ZKPathConfig zkPathConfig, String serviceAddress) throws Exception {
        if (CuratorUtil.checkExists(client, zkPathConfig.getServicePath())) {
            String resulst = CuratorUtil.getStringData(client, zkPathConfig.getServicePath());
            if (!StringUtils.isEmpty(resulst)) {
                String[] adressArray = resulst.split("\\,");
                List<String> adressList = new ArrayList();
                for (String adress : adressArray) {
                    adressList.add(adress.trim());
                }
                //remove已经注册过的
                if (adressList.contains(serviceAddress)) {
                    adressList.remove(serviceAddress);
                }
                CuratorUtil.update(client, zkPathConfig.getServicePath(), org.apache.commons.lang.StringUtils.join(adressList, ","));
            }
        }
    }

    /**
     * 获取服务端提供地址
     * @param serviceName
     * @return
     * @throws Exception
     */
    public String getServiceAddress(String serviceName) throws Exception {
        ZKPathConfig zkPathConfig=new ZKPathConfig(serviceName,null);
        if (CuratorUtil.checkExists(client, zkPathConfig.getServicePath())) {
            return CuratorUtil.getStringDataAndWatch(client, zkPathConfig.getServicePath());
        }
        throw new Exception(serviceName+":has no server");
    }

    public void initListener(ZookeeperDataWatcher zookeeperDataWatcher) {
        client.getCuratorListenable().addListener(zookeeperDataWatcher);
    }
}
