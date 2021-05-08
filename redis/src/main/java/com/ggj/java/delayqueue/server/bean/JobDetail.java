package com.ggj.java.delayqueue.server.bean;

import lombok.Data;

/**
 * @author gaoguangjin
 */
@Data
public class JobDetail {
    /**
     * 任务ID 唯一
     * job id是由业务使用方决定的，一定要保证全局唯一性。这里建议采用topic＋业务唯一id的组合
     */
    private String jobId;
    /**
     * 任务topic
     */
    private String topic;
    /**
     * 延迟执行时间
     */
    private long dealy;
    /**
     * 超时时间
     */
    private long ttr;
    /**
     * 业务数据
     */
    private String body;
    /**
     * job状态
     * @see JobStatusEnum
     */
    private Integer jobStatus;
}
