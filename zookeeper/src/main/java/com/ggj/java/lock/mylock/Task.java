package com.ggj.java.lock.mylock;

import org.apache.commons.lang3.time.DateUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * author:gaoguangjin
 * Description:场景是这样的，项目里面存在一个定时任务，但是在集群环境里面只需要某一个执行。 独占锁
 * Email:335424093@qq.com
 * Date 2016/2/16 11:37
 */
public class Task {

    //同事启动四个
    public static void main(String[] args) throws Exception {
        task();
    }

    /**
     * 解决分布式定时任务只让执行一次。
     * @throws Exception
     */
    private static void task() throws Exception {
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            // define the job and tie it to our HelloJob class
            JobDetail job = JobBuilder.newJob(SimpleJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    //每一秒钟执行一次
                    //.withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever())
                    //20秒执行一次
                    .startAt(DateUtils.parseDate("2016-02-16 14:14:00","yyyy-MM-dd HH:mm:ss"))
                  //  .withSchedule(CronScheduleBuilder.cronSchedule("0/50 * * * * ?"))
                    .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);

           // scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }

    }
}
