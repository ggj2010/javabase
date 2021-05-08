package com.ggj.java.rpc.demo.spring.firstdemo.register.zookeeper.client;

import com.ggj.java.rpc.demo.netty.usezk.util.Constants;
import lombok.Data;

/**
 * @author gaoguangjin
 */
@Data
public class ZKPathConfig {
    private String servicePath;
    private String appPath;
    private String weightPath;
    private String serviceAddress;
    public ZKPathConfig( String serviceName,String serviceAddress) {
        this.servicePath = String.format("%s/%s", Constants.SERVER_PATH,serviceName);
        this.appPath = String.format("%s/%s", Constants.APP_PATH,System.getProperty(Constants.APP_NAME_KEY));
        this.weightPath = String.format("%s/%s", Constants.WEIGHT_PATH,serviceName);
        this.serviceAddress=serviceAddress;
    }
}
