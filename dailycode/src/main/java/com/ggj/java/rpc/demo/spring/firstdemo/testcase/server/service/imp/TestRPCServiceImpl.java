package com.ggj.java.rpc.demo.spring.firstdemo.testcase.server.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.annation.GGJRPCService;
import com.ggj.java.rpc.demo.spring.firstdemo.testcase.server.pojo.TestBean;
import com.ggj.java.rpc.demo.spring.firstdemo.testcase.server.service.TestRPCService;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * @author gaoguangjin
 */
@GGJRPCService(version="0.01",group ="group" )
public class TestRPCServiceImpl implements TestRPCService {
    @Override
    public String getNowTime(String name) {
        return DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String save(TestBean testBean) {
        return JSONObject.toJSONString(testBean);
    }
}
