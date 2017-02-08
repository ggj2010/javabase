package com.ggj.httpclient.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author:gaoguangjin
 * @Description: 使用HttpClient发送请求、接收响应很简单，一般需要如下几步即可。
 * 1. 创建HttpClient对象。
 * 2. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
 * 3. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HetpParams params)方法来添加请求参数；对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
 * 4. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
 * 5. 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。程序可通过该对象获取服务器的响应内容。
 * 6. 释放连接。无论执行方法是否成功，都必须释放连接
 * @Email:335424093@qq.com
 * @Date 2016/4/1 17:53
 */
@Slf4j
public class GetClient {
    private int setSoTimeOut = 10000; // 单位毫秒
    private int setConnectionTimeOut = 10000; // 连接超时，单位毫秒

    private HashMap<String, String> cookieMapping;// cookie
    private String cookieDomain = "www.tuling123.com"; // cookie域名

    private static final String RESPONSE_CHARSET = "utf-8";
    private static final String REQUEST_CHARSET = "utf-8";


    public void  quickget(){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet("http://www.tuicool.com/articles/yYZjQj");
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            log.info(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void get() throws IOException {
        String requesturl = "http://www.tuling123.com/openapi/api";
        // 声明httpclient 每一个会话有一个独立的httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        StringBuilder params = new StringBuilder();
        params.append(requesturl + "?");
        params.append("key=" + URLEncoder.encode("4b441cb500f431adc6cc0cb650b4a5d0", REQUEST_CHARSET)).append("&");
        params.append("info=" + URLEncoder.encode("4b441cb500f431adc6cc0cb650b4a5d0", REQUEST_CHARSET));
        try {
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); // 请求超时
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000); // 读取超时
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

    public void get2() throws IOException {
        String APIKEY = "4b441cb500f431adc6cc0cb650b4a5d0";
        String INFO = new URLCodec().encode("who are you", "utf-8");
        String requesturl = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
        // 声明httpclient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(requesturl);
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

    /**
     * 此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
     * 1、MaxtTotal是整个池子的大小；
     * 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
     * MaxtTotal=400 DefaultMaxPerRoute=200
     * 而我只连接到http://sishuok.com时，到这个主机的并发最多只有200；而不是400；
     * 而我连接到http://sishuok.com 和 http://qq.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。
     */
    public HttpParams getHttpParams() {
        HttpParams params = new BasicHttpParams();
        // 设置连接超时时间
        Integer CONNECTION_TIMEOUT = 2 * 1000; // 设置请求超时2秒钟 根据业务调整
        Integer SO_TIMEOUT = 2 * 1000; // 设置等待数据超时时间2秒钟 根据业务调整
        // 定义了当从ClientConnectionManager中检索ManagedClientConnection实例时使用的毫秒级的超时时间
        // 这个参数期望得到一个java.lang.Long类型的值。如果这个参数没有被设置，默认等于CONNECTION_TIMEOUT，因此一定要设置
        Long CONN_MANAGER_TIMEOUT = 500L; // 该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大 ()

        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
        params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
        return params;
    }

    /**
     *
     * @throws IOException
     */
    //http://jinnianshilongnian.iteye.com/blog/2089792
    public void standard() throws IOException {
      //  CloseableHttpClient httpclient = new DefaultHttpClient(getHttpParams());
        HttpResponse response = null;
        HttpEntity entity = null;
        try {
            HttpGet get = new HttpGet();
            String url = "http://www.baidu.com/";
            get.setURI(new URI(url));
            //执行
            response = getHttpClient().execute(get);
            log.info("status:" + response.getStatusLine());
            entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            //处理响应
        } catch (Exception e) {
            log.error("" + e.getLocalizedMessage());
            //处理异常
        } finally {
            if (response != null) {
                EntityUtils.consume(entity); //会自动释放连接
            }
            //关闭
           // httpclient.getConnectionManager().shutdown();
        }

    }

    /**
     * 多线程的httpclinet
     * @return
     */
    private CloseableHttpClient getThredHttpClient() {
        //单线程跑是基本看不出配置了连接池的好处的，只有使用多线程爬取数据的时候，并且数据量越大效果越明显
        PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();
        conMgr.setMaxTotal(200); // 设置整个连接池最大连接数 根据自己的场景决定
        // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
        conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());// （目前只有一个路由，因此让他等于最大值）
        //另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(conMgr).
                setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
        // 另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).setStaleConnectionCheckEnabled(true).build();
        return httpClient;
    }

    private CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpclient = new DefaultHttpClient(getHttpParams());
        return httpclient;
    }


    /**
     * @param response
     * @return
     * @throws IOException
     */
    private String consumeContent(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        String content = "";
        if (entity != null) {
            content = EntityUtils.toString(entity, "UTF-8");
            long length = entity.getContentLength();

            if (log.isDebugEnabled()) {
                log.debug(" Response content length: " + length);
                log.debug(" Response content : " + content);
            }
            entity.consumeContent();
        }
        return content;
    }


}
