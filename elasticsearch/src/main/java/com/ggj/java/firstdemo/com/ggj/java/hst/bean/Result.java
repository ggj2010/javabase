package com.ggj.java.firstdemo.com.ggj.java.hst.bean;

import lombok.Data;

/**
 * @author gaoguangjin
 */
@Data
public class Result {
    private String score;
    private String cargoDescription;
    private String cargoChineseDescription;

    private String htsCode;
    private String chineseName;
    private String engName;
}
