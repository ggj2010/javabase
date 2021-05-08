package com.ggj.java.distributedtask.core.job.enums;


import lombok.Getter;

/**
 * @author gaoguangjin
 */
public enum JobStatusEnum {
    CREATE(1, "添加"),
    DELETE(2, "删除"),
    UPDATE(3, "更新");
    @Getter
    private int status;
    @Getter
    private String name;

    JobStatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public static JobStatusEnum valueOf(Integer status) {
        switch (status) {
            case 1:
                return CREATE;
            case 2:
                return DELETE;
            default:
                return null;
        }
    }
}
