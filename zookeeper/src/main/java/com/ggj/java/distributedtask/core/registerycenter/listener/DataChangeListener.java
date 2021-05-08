package com.ggj.java.distributedtask.core.registerycenter.listener;

import org.quartz.SchedulerException;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 16:06
 */
public interface DataChangeListener {
    void eventHandler(Object...ojb) throws SchedulerException;
}
