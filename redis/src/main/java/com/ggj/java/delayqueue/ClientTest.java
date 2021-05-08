package com.ggj.java.delayqueue;

import com.ggj.java.delayqueue.server.JobMamager;
import com.ggj.java.delayqueue.server.bean.JobDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * https://tech.youzan.com/queuing_delay/
 *
 * @author gaoguangjin
 */
@Slf4j
public class ClientTest {

    public static void main(String[] args) {

        JobMamager jobMamager = JobMamager.getInstance();
        //mock rpc查询
        String testTopic = "ggj";
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                JobDetail jobDetail = new JobDetail();
                String key = UUID.randomUUID().toString();
                jobDetail.setJobId(key);
                jobDetail.setTopic(testTopic);
                long dealyTime = System.currentTimeMillis() + 1000 * 5;
                jobDetail.setDealy(dealyTime);
                jobMamager.putJob(jobDetail);
                log.info("add dealy job key={},delaytime={}", key, 1000 * 5);
            }

        }).start();


        while (true) {
            try {
                //JobDetail detail = jobMamager.popJob(testTopic);
                //解决同一秒很多订单问题
                List<JobDetail> detailList = jobMamager.popJobList(testTopic);
                if(CollectionUtils.isNotEmpty(detailList)){
                for (JobDetail detail : detailList) {
                    log.info("consume  job key={},delaytime={}", detail.getJobId(), detail.getDealy());
                }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }
}
