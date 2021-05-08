package com.ggj.java.delayqueue.server.bean;

public enum JobStatusEnum {
    READY(1, "可以执行状态"),
    DELAY(2, "不可以执行状态"),
    RESERVED(3, "已被消费者读取，但还未得到消费者的响应,delete、finish"),
    DELETED(4, "已经被消费完成或者已被删除");
    private int status;
    private String detail;

    JobStatusEnum(int status, String detail) {
        this.status = status;
        this.detail = detail;
    }
}
