package com.ggj.java.distributedtask.core.job;

import com.ggj.java.distributedtask.core.job.enums.JobExcuteStatusEnum;
import com.ggj.java.distributedtask.core.job.vo.JobConfig;
import com.ggj.java.distributedtask.core.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quartz定时任务默认都是并发执行的，不会等待上一次任务执行完毕，只要间隔时间到就会执行,
 * 如果定时任执行太长，会长时间占用资源，导致其它任务堵塞。
 *
 * @DisallowConcurrentExecution 禁止并发执行多个相同定义的JobDetail,
 * @author:gaoguangjin
 * @date 2016/9/2 16:21
 */
@Slf4j
public class JobManager {
    /**
     * 停机任务执行等待时间
     */
    private final long waitAllJobExcuteTimeout = 120;
    /**
     * 缓存已添加的job
     */
    private static final Map<String, String> cacheJobName = new ConcurrentHashMap<>();
    private static Scheduler scheduler;

    static {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("创建StdSchedulerFactory error" + e.getLocalizedMessage());
        }
    }

    /**
     * 提交任务
     *
     * @param jobConfig
     */
    public static void submitJob(JobConfig jobConfig) throws SchedulerException {
        try {
            if (!cacheJobName.containsKey(jobConfig.getJobName())) {
                JobDetail job = JobBuilder.newJob(jobConfig.getJobClass()).withIdentity(jobConfig.getJobName(), jobConfig.getGroupName()).build();
                job.getJobDataMap().put(Constants.TASK_EXCUETE_TIMEOUT_KEY, jobConfig.getExcuteTimeOut());
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobConfig.getTriggerKey(), jobConfig.getGroupName()).withSchedule(CronScheduleBuilder.cronSchedule(jobConfig.getJobCron()))
                        .build();
                scheduler.scheduleJob(job, trigger);
                cacheJobName.put(jobConfig.getJobName(), jobConfig.getJobName());
            }
        } catch (SchedulerException e) {
            log.error("initJob error" + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * 初始化job
     * 停止对job就不需要创建了
     *
     * @param jobStatus
     * @param jobConfig
     * @throws SchedulerException
     */
    public static void initJob(Integer jobStatus, JobConfig jobConfig) throws SchedulerException {
        JobExcuteStatusEnum jobExcuteStatusEnum = JobExcuteStatusEnum.valueOf(jobStatus);
        submitJob(jobConfig);
        switch (jobExcuteStatusEnum) {
            case RUN:
                runJob(jobConfig);
                break;
            case STOP:
                deleteJob(jobConfig);
                break;
            case PAUSE:
                pauseJob(jobConfig);
                break;
            default:
                return;
        }
    }

    /**
     * 删除job
     *
     * @param jobConfig
     */
    public static void deleteJob(JobConfig jobConfig) {
        try {
            TriggerKey jobKey = new TriggerKey(jobConfig.getTriggerKey(), jobConfig.getGroupName());
            if (scheduler.checkExists(jobKey)) {
                scheduler.unscheduleJob(jobKey);
            }
            cacheJobName.remove(jobConfig.getJobName());
        } catch (SchedulerException e) {
            log.error("deleteJob error" + e.getLocalizedMessage());
        }
    }

    /**
     * 再启动
     *
     * @param jobConfig
     */
    public static void runJob(JobConfig jobConfig) {
        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(new TriggerKey(jobConfig.getTriggerKey(), jobConfig.getGroupName()));
            JobKey jobKey = new JobKey(jobConfig.getJobName(), jobConfig.getGroupName());
            if (scheduler.checkExists(jobKey)) {
                if (triggerState.equals(Trigger.TriggerState.NONE)) {
                    submitJob(jobConfig);
                } else {
                    scheduler.rescheduleJob(new TriggerKey(jobConfig.getTriggerKey(), jobConfig.getGroupName()), scheduler.getTrigger(new TriggerKey(jobConfig.getTriggerKey(), jobConfig.getGroupName())));
                }
            }
        } catch (SchedulerException e) {
            log.error("deleteJob error" + e.getLocalizedMessage());
        }
    }

    /**
     * 暂停job
     *
     * @param jobConfig
     */
    public static void pauseJob(JobConfig jobConfig) {
        try {
            JobKey jobKey = new JobKey(jobConfig.getJobName(), jobConfig.getGroupName());
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            log.error("pauseJob error" + e.getLocalizedMessage());
        }
    }

    /**
     * 更新job 执行频率
     *
     * @param jobConfig
     */
    public static void updateJob(JobConfig jobConfig) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobConfig.getTriggerKey(), jobConfig.getGroupName()).withSchedule(CronScheduleBuilder.cronSchedule(jobConfig.getJobCron()))
                    .build();
            if (scheduler.checkExists(trigger.getKey())) {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException e) {
            log.error("rescheduleJob error", e);
        }
    }

    /**
     * 等待所有任务都执行完
     */
    public static void waitAllJobExcute() {
        waitAllJobExcute(0);
    }

    /**
     * 等待指定时间任务都执行。
     *
     * @param time
     */
    public static void waitAllJobExcute(long time) {
        long endTime = System.currentTimeMillis() + time * 1000;
        try {
            while (true) {
                List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
                if (CollectionUtils.isEmpty(executingJobs)) {
                    break;
                }
                if (time != 0 && System.currentTimeMillis() > endTime) {
                    log.info("{},{}", System.currentTimeMillis(), endTime);
                    log.info("超时推出，剩余待执行任务{}", executingJobs.size());
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (SchedulerException e) {
            log.error("waitAllJobExcute error", e);
        } catch (InterruptedException e) {
            log.error("sleep error", e);
        }
    }
}
