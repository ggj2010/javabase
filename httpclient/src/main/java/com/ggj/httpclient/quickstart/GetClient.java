package com.ggj.httpclient.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/1 17:53
 */
@Slf4j
public class GetClient {
    private int setSoTimeOut = 10000; // 单位毫秒
    private int setConnectionTimeOut = 10000; // 连接超时，单位毫秒

    private HashMap<String, String> cookieMapping;//cookie
    private String cookieDomain = "www.tuling123.com"; // cookie域名

    private static final String RESPONSE_CHARSET = "utf-8";
    private static final String REQUEST_CHARSET = "utf-8";


    public void get() throws IOException {
        String requesturl = "http://www.tuling123.com/openapi/api";
        //声明httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        StringBuilder params = new StringBuilder();
        params.append(requesturl + "?");
        params.append("key=" + URLEncoder.encode("4b441cb500f431adc6cc0cb650b4a5d0", REQUEST_CHARSET)).append("&");
        params.append("info=" + URLEncoder.encode("4b441cb500f431adc6cc0cb650b4a5d0", REQUEST_CHARSET));
        try {
//            httpclient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, setSoTimeOut);
//            httpclient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, setConnectionTimeOut);

            HttpGet httpGet = new HttpGet(params.toString());
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            log.info(content);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("" + e);
        } finally {
            if (response != null)
                response.close();
        }

    }
}
