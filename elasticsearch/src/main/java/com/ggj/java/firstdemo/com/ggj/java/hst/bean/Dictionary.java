package com.ggj.java.firstdemo.com.ggj.java.hst.bean;

import lombok.Data;

/**
 * @author gaoguangjin
 */
@Data
public class Dictionary {
    private String chineseName;
    private String engName;
    private String htsCode;

    public Dictionary() {
    }

    public Dictionary(String chineseName, String engName, String htsCode) {
        this.chineseName = chineseName;
        this.engName = engName;
        this.htsCode = htsCode;
    }
}
