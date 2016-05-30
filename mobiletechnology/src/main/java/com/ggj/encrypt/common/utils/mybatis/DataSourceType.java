package com.ggj.encrypt.common.utils.mybatis;

/**
 * @author:gaoguangjin
 * @date 2016/5/30 14:36
 */
public enum DataSourceType {
    read("从库"),write("主库");
    private String name;

    DataSourceType(String name) {
        this.name=name;
    }
}
