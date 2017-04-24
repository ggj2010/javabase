package com.ggj.encrypt.modules.base.controller;

import com.ggj.encrypt.modules.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @Description: 测试rest的api
 * @Email:335424093@qq.com
 * @Date 2016/4/28 15:31
 */
@Slf4j
public class RestTemplateControllerTest extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;
//    RestTemplate restTemplate = new TestRestTemplate();
    private final String TEST_URL_ONE = "http://localhost/test/{sign}/{id}";
    private final String TEST_URL_TWO = "http://localhost/exception/{type}";

    @Test
    public void testRestTemplateClient() throws Exception {
        String[] uriVariables={"aaaa签名","001"};
        ResponseEntity<String> exchange = restTemplate.exchange(TEST_URL_ONE, HttpMethod.POST, null, String.class,uriVariables);
        log.info(exchange.getBody());
         assertEquals(exchange.getStatusCode(), HttpStatus.OK);
    }

    /**
     * 测试一次捕获
     * @throws Exception
     */
    @Test
    public void testTestException() throws Exception {
        Map<String, String> uriVariables=new HashMap<String, String>();
        uriVariables.put("type","1");
        ResponseEntity<String> responseEntity=restTemplate.getForEntity(TEST_URL_TWO,String.class,uriVariables);
        log.info(responseEntity.getBody());
        assertEquals(responseEntity.getBody(),"{\"code\":\"-1\",\"msg\":\"system bus\"}");
    }
}