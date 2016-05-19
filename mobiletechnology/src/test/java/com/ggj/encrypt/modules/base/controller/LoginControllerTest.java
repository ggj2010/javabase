package com.ggj.encrypt.modules.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ggj.encrypt.common.constant.GlobalConstant;
import com.ggj.encrypt.common.utils.ApiTools;
import com.ggj.encrypt.common.utils.CalcKeyIvUtils;
import com.ggj.encrypt.common.utils.DesUtil;
import com.ggj.encrypt.modules.BaseTest;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/28 11:13
 */
@Slf4j
public class LoginControllerTest extends BaseTest {
    RestTemplate restTemplate = new TestRestTemplate();
    private final String LOGIN_URL = "http://localhost/login/{p}";

    //32 位
    private final String APPKEY="1234567890_1234567890_1234appkey";
    private String TIMESTAMP="";
    //用戶名和密码
    public final static String LOGINNAME="gaoguangjin";
    public final static String PASSWORD="qazqaz";


    @Test
    public void testLogin() throws Exception {
        String[] uriVariables=getLoginParam();
        HttpHeaders headers =getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, entity, String.class,uriVariables);
        List<String> token = exchange.getHeaders().get(GlobalConstant.USER_TONKEN);
        log.info("============返回结果================");
        if(!CollectionUtils.isEmpty(token)){
            //token解密后打印出来
            log.info("打印token="+DesUtil.decrypt(token.get(0),APPKEY));
        }
        List<String> userId = exchange.getHeaders().get(GlobalConstant.USE_ID);
        if(!CollectionUtils.isEmpty(token)){
            //token解密后打印出来
            log.info("打印userId="+DesUtil.decrypt(userId.get(0),APPKEY));
        }
        log.info("打印返登陆回结果"+exchange.getBody());
    }

    /**
     * 密码des加密
     * @return
     * @throws Exception
     */
    private String[] getLoginParam() throws Exception {
        TIMESTAMP=ApiTools.getTimeStamp()+"";
        UserInfo userInfo=new UserInfo();
        userInfo.setLoginName(LOGINNAME);
        userInfo.setPassword(PASSWORD);
        String p=DesUtil.encrypt(JSON.toJSONString(userInfo),APPKEY+TIMESTAMP);
        return new String[]{p};
    }

    /**
     * header 存放TIMESTAMP和APPKEY
     * @return
     */
    public HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(GlobalConstant.TIMESTAMP, TIMESTAMP);
        httpHeaders.add(GlobalConstant.APPKEY,APPKEY);
        return httpHeaders;
    }
}