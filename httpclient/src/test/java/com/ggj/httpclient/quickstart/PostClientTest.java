package com.ggj.httpclient.quickstart;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/6 15:37
 */
public class PostClientTest {
    PostClient postClient;
    @Before
    public void before(){
        postClient= new PostClient();
    }

    @Test
    public void testPost() throws Exception {
        postClient.post();
    }

    @Test
    public void testGzip() throws Exception {
        postClient.gzip();
    }
}