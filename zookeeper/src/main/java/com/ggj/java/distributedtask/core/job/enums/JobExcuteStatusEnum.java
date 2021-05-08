package com.ggj.java.distributedtask.core.job.enums;


import lombok.Getter;

/**
 * job状态
 * @author:gaoguangjin
 * @date 2016/9/2 17:47
 */

public enum JobExcuteStatusEnum {
    RUN(4, "执行"),
    PAUSE(5, "暂停"),
    STOP(6, "停止");
    @Getter
    private int status;
    @Getter
    private String name;

    JobExcuteStatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public static JobExcuteStatusEnum valueOf(int status) {
        switch (status) {
            case 4:
                return RUN;
            case 5:
                return PAUSE;
            case 6:
                return STOP;
            default:
                return null;
        }
    }
}
