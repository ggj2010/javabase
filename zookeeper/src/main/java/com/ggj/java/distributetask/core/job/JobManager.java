package com.ggj.java.distributetask.core.job;

import com.ggj.java.distributetask.client.ClientOneJob;
import com.ggj.java.lock.mylock.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 16:21
 */
@Slf4j
public class JobManager {
    private static Scheduler scheduler ;
    static {
        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("创建StdSchedulerFactory error"+e.getLocalizedMessage());
        }
    }

    public static void initJob(JobConfig jobConfig) {
        try {
            JobDetail job = JobBuilder.newJob(ClientOneJob.class).withIdentity(jobConfig.getJobName(), jobConfig.getGroupName()).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobConfig.getTriggerKey(), jobConfig.getGroupName()).withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("initJob error"+e.getLocalizedMessage());
        }
    }


    public static void deleteJob(JobConfig jobConfig) {
        try {
            scheduler.deleteJob(new JobKey(jobConfig.getJobName(), jobConfig.getGroupName()));
        } catch (SchedulerException e) {
            log.error("deleteJob error"+e.getLocalizedMessage());
        }
    }

    public static void pauseJob(JobConfig jobConfig) {
        try {
            scheduler.pauseJob(new JobKey(jobConfig.getJobName(), jobConfig.getGroupName()));
        } catch (SchedulerException e) {
            log.error("pauseJob error"+e.getLocalizedMessage());
        }
    }

}
