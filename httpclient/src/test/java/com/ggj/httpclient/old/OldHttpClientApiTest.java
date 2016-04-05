package com.ggj.httpclient.old;


import org.junit.Before;
import org.junit.Test;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/1 18:52
 */
public class OldHttpClientApiTest {

    OldHttpClientApi old;
    @Before
    public void before(){
         old=new OldHttpClientApi();
    }

    @Test
    public void testGet2() throws Exception {
        old.get2();
    }

    @Test
    public void testGet() throws Exception {
        old.get();
    }

    @Test
    public void testPost() throws Exception {
        old.post();
    }
}