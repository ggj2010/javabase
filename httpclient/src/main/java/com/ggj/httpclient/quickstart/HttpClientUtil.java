package com.ggj.httpclient.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/6 15:05
 */
@Slf4j
public class HttpClientUtil {

    private static final int TIMEOUT = 3000;
    private static final int MAXTOTALCONNECTIONS = 400;
    private static final int CONNECTIONMANAGERTIMEOUT = 10000;
    private static final String CHARSET = "UTF-8";

    private HttpClientUtil() {
    }

    public static String httpPost(String url, Map<String, Object> paramMap, long timer) throws Exception {
        String content = httpPostGZip(url, paramMap, "UTF-8", timer);
        return content;
    }

    private static String httpPostGZip(String url, Map<String, Object> paramMap, String code, long timer) {
        long funTimer = System.currentTimeMillis();
        log.info(timer + " request[" + url + "]  param[" + (paramMap == null?"NULL":paramMap.toString()) + "]");
        String content = null;
        if(StringUtils.isEmpty(url)) {
            return null;
        } else {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost method = new HttpPost(url);
            method.setHeader("Connection", "close");
            method.setHeader("Accept-Encoding", "gzip, deflate");
            method.setHeader("Accept", "text/plain");

            try {
                UrlEncodedFormEntity e = new UrlEncodedFormEntity(getParamsList(paramMap), code);
                method.setEntity(e);
                HttpResponse response = httpclient.execute(method);
                int status = response.getStatusLine().getStatusCode();
                log.info(timer + " status=" + status);
                if(status == 200) {
                    boolean httpEntity = false;
                    Header[] headers = response.getHeaders("Content-Encoding");
                    if(headers != null && headers.length > 0) {
                        Header[] httpEntity1 = headers;
                        int is = headers.length;

                        for(int gzin = 0; gzin < is; ++gzin) {
                            Header isr = httpEntity1[gzin];
                            if(isr.getValue().toLowerCase().indexOf("gzip") != -1) {
                                httpEntity = true;
                            }
                        }
                    }

                    log.info(timer + " isGzip=" + httpEntity);
                    HttpEntity var28 = response.getEntity();
                    if(httpEntity) {
                        InputStream var29 = var28.getContent();
                        GZIPInputStream var30 = new GZIPInputStream(var29);
                        InputStreamReader var31 = new InputStreamReader(var30, code);
                        BufferedReader br = new BufferedReader(var31);
                        StringBuffer sb = new StringBuffer();

                        String tempbf;
                        while((tempbf = br.readLine()) != null) {
                            sb.append(tempbf);
                            sb.append("\r\n");
                        }

                        var31.close();
                        var30.close();
                        content = sb.toString();
                    } else {
                        content = EntityUtils.toString(var28);
                    }
                } else if(status == 500) {
                    HttpEntity var27 = response.getEntity();
                    content = EntityUtils.toString(var27);
                } else {
                    method.abort();
                }
            } catch (Exception var25) {
                method.abort();
                log.error(timer + " request[" + url + "] ERROR", var25);
            } finally {
                if(!method.isAborted()) {
                    httpclient.getConnectionManager().shutdown();
                }

            }

            log.info(timer + " request[" + url + "] cost:" + (System.currentTimeMillis() - funTimer));
            log.info(timer + " request[" + url + "] response[" + content + "]");
            return content;
        }
    }

    private static List getParamsList(Map<String, Object> paramsMap) {
        if(paramsMap != null && paramsMap.size() != 0) {
            ArrayList params = new ArrayList();
            Iterator i$ = paramsMap.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry map = (Map.Entry)i$.next();
                params.add(new BasicNameValuePair((String)map.getKey(), map.getValue().toString()));
            }
            return params;
        } else {
            return null;
        }
    }

}
