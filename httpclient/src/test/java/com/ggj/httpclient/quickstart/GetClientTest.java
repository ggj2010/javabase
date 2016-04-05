package com.ggj.httpclient.quickstart;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/5 18:04
 */
public class GetClientTest {
    GetClient getClient;
@Before
public  void before(){
     getClient=new GetClient();
}
    @Test
    public void testGet() throws Exception {
        getClient.get();
    }
}