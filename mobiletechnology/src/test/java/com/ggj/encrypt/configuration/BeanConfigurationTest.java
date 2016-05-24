package com.ggj.encrypt.configuration;

import com.ggj.encrypt.modules.BaseTest;
import com.ggj.encrypt.modules.sys.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @date 2016/5/23 15:59
 */
@Slf4j
public class BeanConfigurationTest extends BaseTest {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void testGetExecutor() throws Exception {
        //循环是为了MonitorThread 打印日志
        for (int i = 0; i <200 ; i++) {
            threadPoolTaskExecutor.execute(new MyTask());
        }
    }

    class MyTask implements Runnable{
        @Override
        public void run() {
            log.info("====threadPoolTaskExecutor 加载成功");
            }
    }
}