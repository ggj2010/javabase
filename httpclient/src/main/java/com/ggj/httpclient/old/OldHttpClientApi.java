package com.ggj.httpclient.old;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;

import java.beans.Encoder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author:gaoguangjin
 * @Description:org.apache.commons.httpclient老版本httpclient 记录
 * @Email:335424093@qq.com
 * @Date 2016/4/1 18:13
 */
@Slf4j
public class OldHttpClientApi {

    private static final int BUFFER_SIZE = 4096;

    public void  post(){
        String requesturl = "http://www.tuling123.com/openapi/api";
        PostMethod postMethod = new PostMethod(requesturl);
        String APIKEY = "4b441cb500f431adc6cc0cb650b4a5d0";
        String INFO ="北京时间";
        NameValuePair nvp1=new NameValuePair("key",APIKEY);
        NameValuePair nvp2=new NameValuePair("info",INFO);
        NameValuePair[] data = new NameValuePair[2];
        data[0]=nvp1;
        data[1]=nvp2;

        postMethod.setRequestBody(data);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
        try {
            byte[] responseBody = executeMethod(postMethod,10000);
            String content=new String(responseBody ,"utf-8");
            log.info(content);
        }catch (Exception e){
           log.error(""+e.getLocalizedMessage());
        }finally {
            postMethod.releaseConnection();
        }
    }




    public void  get() throws UnsupportedEncodingException {
        HttpClient client = new HttpClient();
        String APIKEY = "4b441cb500f431adc6cc0cb650b4a5d0";
        String INFO =new URLCodec().encode("who are you","utf-8");
        String requesturl = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
        GetMethod getMethod = new GetMethod(requesturl);
        try {
            int stat = client.executeMethod(getMethod);
            if (stat != HttpStatus.SC_OK)
               log.error("get失败！");
            byte[] responseBody = getMethod.getResponseBody();
            String content=new String(responseBody ,"utf-8");
            log.info(content);
        }catch (Exception e){
           log.error(""+e.getLocalizedMessage());
        }finally {
            getMethod.releaseConnection();
        }
    }


    public void  get2(){
        String requesturl = "http://www.tuling123.com/openapi/api";
        GetMethod getMethod = new GetMethod(requesturl);
        try {
            String APIKEY = "4b441cb500f431adc6cc0cb650b4a5d0";
            String INFO ="北京时间";

            //get  参数
            NameValuePair nvp1=new NameValuePair("key",APIKEY);
            NameValuePair nvp2=new NameValuePair("info",INFO);
            NameValuePair[] nvp = new NameValuePair[2];
            nvp[0]=nvp1;
            nvp[1]=nvp2;
            String params=EncodingUtil.formUrlEncode(nvp, "UTF-8");
            //设置参数
            getMethod.setQueryString(params);

            byte[] responseBody = executeMethod(getMethod,10000);

             String content=new String(responseBody ,"utf-8");
            log.info(content);
        }catch (Exception e){
           log.error(""+e.getLocalizedMessage());
        }finally {
            //结束一定要关闭
            getMethod.releaseConnection();
        }
    }


    private static byte[] executeMethod(HttpMethodBase method, int timeout) throws Exception {
        InputStream in = null;
        try {
            method.addRequestHeader("Connection", "close");
            HttpClient client = new HttpClient();
            HttpConnectionManagerParams params = client.getHttpConnectionManager().getParams();
            //设置连接时候一些参数
            params.setConnectionTimeout(timeout);
            params.setSoTimeout(timeout);
            params.setStaleCheckingEnabled(false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);

            int stat =  client.executeMethod(method);
            if (stat != HttpStatus.SC_OK)
                log.error("get失败！");

            //method.getResponseBody()
            in = method.getResponseBodyAsStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
