package com.ggj.encrypt.modules.sys.controller;

import com.ggj.encrypt.common.constant.GlobalConstant;
import com.ggj.encrypt.common.utils.ApiTools;
import com.ggj.encrypt.common.utils.DesUtil;
import com.ggj.encrypt.common.utils.MD5Util;
import com.ggj.encrypt.modules.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/5/3 15:56
 */
@Slf4j
public class OrderControllerTest extends BaseTest{
    private final String FINDUSERINFO_URL = "http://localhost/mobile/order/orderList/{currentPage}/{pageSize}";
    RestTemplate restTemplate = new TestRestTemplate();


    //32 位
    private final String APPKEY="1234567890_1234567890_1234appkey";
    private final String USERTOKEN="f4c04b7a8ec84aa9bdca189f682fba71";
    private String TIMESTAMP="";
    //测试用id=1 的账户
    private String USERID="1";
    private String currentPage="1";
    private String pageSize="10";

    @Test
    public void testOrderList() throws Exception {
        String[] uriVariables=getParam();
        HttpHeaders headers =getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(FINDUSERINFO_URL, HttpMethod.POST, entity, String.class,uriVariables);
        log.info("============返回结果================");
        log.info("打印返登陆回结果"+exchange.getBody());
    }

    /**
     * @return
     * @throws Exception
     */
    private String[] getParam() throws Exception {
        TIMESTAMP= ApiTools.getTimeStamp()+"";
        return new String[]{currentPage,pageSize};
    }

    /**
     * header
     * @return
     */
    public HttpHeaders getHttpHeaders() throws Exception {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(GlobalConstant.TIMESTAMP, TIMESTAMP);
        httpHeaders.add(GlobalConstant.APPKEY,APPKEY);
        httpHeaders.add(GlobalConstant.USE_ID,DesUtil.encrypt(USERID,APPKEY));
        httpHeaders.add(GlobalConstant.SIGN,MD5Util.md5Encode(APPKEY+TIMESTAMP+USERTOKEN));

        return httpHeaders;
    }


}