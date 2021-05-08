package com.ggj.java.distributedtask.core.annation;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 9:45
 */

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeJob {
     /**
      * 任务名称
      * @return
      */
     String jobName();
     /**
      * 任务组名称
      * @return
      */
     String groupName();
     /**
      * 任务执行器名称
      * @return
      */
     String triggerKey();
     /**
      * 任务描述
      * @return
      */
     String jobDetail();
     /**
      * 任务cron表达式
      * @return
      */
     String jobCron();

     /**
      * 任务执行超时时间 分钟
      * @return
      */
     long excuteTimeOut()  default 60;
}
