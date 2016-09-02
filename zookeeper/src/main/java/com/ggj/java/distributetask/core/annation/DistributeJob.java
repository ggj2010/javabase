package com.ggj.java.distributetask.core.annation;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 9:45
 */

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeJob {
     String clientId() default "";
     String jobName();
     String groupName();
     String triggerKey();
     String jobDetail();
     String jobCron();
}
