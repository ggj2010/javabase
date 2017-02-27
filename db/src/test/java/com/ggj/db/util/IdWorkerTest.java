package com.ggj.db.util;


import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;


/**
 * @author:gaoguangjin
 * @date 2017/2/22 17:09
 */
@Slf4j
public class IdWorkerTest {

    public static void main(String[] args) {
        try {
            getId();
        } catch (Exception e) {
            log.error("生成id错误");
        }
    }
    public static  void getId() throws Exception {
        IdWorker iw = new IdWorker(0,1);
        Set<Long> set=new HashSet<Long>();
        CountDownLatch countDownLatch=new CountDownLatch(100);
        long beginTime = System.currentTimeMillis();
        //100w个数据
        int idSize=10000*100;
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                for(int j=0;j<10000;j++){
                    long id=iw.getId();
                    if(!set.add(id)){
                        log.info("重复了：{}",id);
                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        log.info("生成：{}个id 耗时：{}ms",idSize,(endTime-beginTime));
    }

}