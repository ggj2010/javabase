package com.ggj.httpclient.testperformance;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author:gaoguangjin
 * @Description:  CloseableHttpClient httpclient = HttpClients.createDefault(); 应该是一个【会话】用一个httpClient实例
 * @Email:335424093@qq.com
 * @Date 2016/4/6 13:45
 */
@Slf4j
public class HttpClientPerformance {

    public static void main(String[] args) throws IOException {
        HttpClientPerformance hcp=new HttpClientPerformance();
        //创建 CloseableHttpClient httpclient = HttpClients.createDefault() 是需要耗时的,可以共享一个httpclient
        //14:56:05.745 [main] INFO  c.g.h.t.HttpClientPerformance - methodOne耗费时间：10873
        //14:56:08.807 [main] INFO  c.g.h.t.HttpClientPerformance - methodTwo耗费时间：3052

        hcp.methodOne();
        hcp.methodTwo();
    }
    public void methodOne() throws IOException {
        long beginTime=System.currentTimeMillis();
        for (int i = 0; i <100 ; i++) {
            methodExecute(null);
        }
        long endTime=System.currentTimeMillis();
        log.info("methodOne耗费时间："+(endTime-beginTime));
    }


    /**
     * 公用一个上下文
     * @throws IOException
     */
    public void methodTwo() throws IOException {
        long beginTime=System.currentTimeMillis();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        for (int i = 0; i <100 ; i++) {
            methodExecute(httpclient);
        }
        long endTime=System.currentTimeMillis();
        log.info("methodTwo耗费时间："+(endTime-beginTime));
    }

    public static void methodExecute(CloseableHttpClient httpclient) throws IOException {
        HttpResponse response = null;
        HttpEntity entity = null;
        String url = "http://www.baidu.com/";
        HttpGet httpGet=new HttpGet(url);
        try {
            if(httpclient==null){
                 httpclient = HttpClients.createDefault();
            }
            response = httpclient.execute(httpGet);
          //  log.info("status:" + response.getStatusLine());
            entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            log.error(""+e.getLocalizedMessage());
        }finally {
            if (response != null) {
                EntityUtils.consume(entity); //会自动释放连接
            }
        }
    }

}
