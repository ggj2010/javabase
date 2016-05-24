package com.ggj.encrypt.common.utils.kafka;

import com.alibaba.fastjson.JSONObject;
import com.ggj.encrypt.common.utils.ApiTools;
import com.ggj.encrypt.common.utils.DateUtils;
import com.ggj.encrypt.modules.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @date 2016/5/24 17:34
 */
public class KafkaClientUtilTest extends BaseTest {
    @Autowired
    private KafkaClientUtil kafkaClientUtil;

    @Test
    public void testSend() throws Exception {
        //kafka 发送不能用多线程
        kafkaClientUtil.send("{\"ip\":\"127.0.0.1\",\"paramers\":\"参数:\",\"time\":\"2016-05-24 17:44:35\",\"url\":\"/mobile/order/orderList/1/10\"} ");
    }
}