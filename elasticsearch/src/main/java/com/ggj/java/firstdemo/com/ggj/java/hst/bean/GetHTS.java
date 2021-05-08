package com.ggj.java.firstdemo.com.ggj.java.hst.bean;

import lombok.Data;

/**
 * @author gaoguangjin
 */
@Data
public class GetHTS {
    private String engName;
    private String chineseName;

    @Override
    public String toString() {
        return "GetHTS{" +
                "engName='" + engName + '\'' +
                ", chineseName='" + chineseName + '\'' +
                '}';
    }
}
