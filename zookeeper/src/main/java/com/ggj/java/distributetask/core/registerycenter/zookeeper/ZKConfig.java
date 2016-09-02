package com.ggj.java.distributetask.core.registerycenter.zookeeper;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 10:36
 */
@Getter
@Setter
public class ZKConfig {
    private String connectString;
    private String nameSpace;
    public ZKConfig(String connectString, String nameSpace){
        this.connectString=connectString;
        this.nameSpace=nameSpace;
    }
}
