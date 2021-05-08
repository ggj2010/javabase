package com.ggj.java.delayqueue.server.common;

/**
 * @author gaoguangjin
 */
public class Constants {
    public final static String JOB_DETAIL_KEY="job:detail:";
    public final static String JOB_DELAY_KEY="job:delay:";
    public final static String JOB_READY_TOPIC_KEY="job:readytopic:";
    public final static String JOB_BUCKET_KEY="job:bucket:";
    public final static int MAX_BUCKET_SIZE=10;
    public final static int LOCK_TIME=10;
    public final static String JOB_LOCK_KEY="job:lock";
    public final static String OK="ok";
}
