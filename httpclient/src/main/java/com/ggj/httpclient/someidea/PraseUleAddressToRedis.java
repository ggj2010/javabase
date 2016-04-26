package com.ggj.httpclient.someidea;

import com.ggj.httpclient.someidea.redis.RedisDaoTemplate;
import com.ggj.httpclient.someidea.redis.callback.RedisCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author:gaoguangjin
 * @Description:爬去邮乐网会员地址，放入redis
 * @Email:335424093@qq.com
 * @Date 2016/4/7 18:46
 */
@Slf4j
public class PraseUleAddressToRedis {
    private static final String LOGIN_URL = "http://m.ule.com:80/user/login";
    private static final String INDEX_URL = "http://m.ule.com/";
    private static final String ADDRESS_URL = "http://m.ule.com/address/detail/";
    // 多线程httpclient解决超时连接, 设置请求超时2秒钟 根据业务调整 ;设置等待数据超时时间2秒钟 根据业务调整;该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大 ()
    RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(2000).setConnectionRequestTimeout(500)
            .setStaleConnectionCheckEnabled(true).build();

    private int threadNumber = 200;

   private int maxId=2560000;

    private int pageSize = maxId / threadNumber;


    private static final String REDIS_KEY_ADDRESSID_SUCCESS="ule_address_success";
    private static final String REDIS_KEY_ADDRESSID_SUCCESS_LIST="ule_address_success_list";
    private static final String REDIS_KEY_ADDRESSID_ERROR="ule_address_error";
//00:49:32.373 [main] INFO  c.g.h.s.PraseUleAddressToRedis - 执行threadPoolHttp时间：1008033
    public static void main(String[] args) throws IOException {
        new PraseUleAddressToRedis().startTask();
    }

    public void startTask() throws IOException {
        CloseableHttpClient httpclient = getPoolHttpClient();
        // 登陆获取cookie
        Header[] cookieHeaders = login(httpclient);
        startThreadPoolHttp(cookieHeaders, httpclient);
    }

    private void startThreadPoolHttp(Header[] cookieHeaders, CloseableHttpClient httpclient) {
        long beingTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            executorService.execute(executeThreadHttp(httpclient, i));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            long endTime = System.currentTimeMillis();
            log.info("执行threadPoolHttp时间：" + (endTime - beingTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Runnable executeThreadHttp(CloseableHttpClient httpclient, int page) {
        return new Thread(() -> {
            int beginId = pageSize * page;
            int endId = beginId + pageSize;
            try {
                for (int i = beginId; i <= endId; i++) {
                    HttpGet httGet = new HttpGet(ADDRESS_URL+i);
                    //设置访问参数
                    RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).build();
                    httGet.setConfig(defaultRequestConfig);
                     // 多线程公用一个httpclient会导致串行,一个Http连接在同一时间只能被一个线程访问
                    CloseableHttpResponse resopnse = httpclient.execute(httGet);
                    // 休眠模拟多个线程抢占httpclient
                    HttpEntity entity = resopnse.getEntity();
                    String result = EntityUtils.toString(entity, "utf-8");
                    praseResult(result,i);
                    EntityUtils.consume(entity); // 会自动释放连接
                }
            } catch (IOException e) {
                log.error("" + e.getLocalizedMessage());
            }
        });
    }


    public Header[] login(CloseableHttpClient httpclient) throws IOException {
        HttpGet httpGet = new HttpGet(INDEX_URL);
        HttpResponse responsePost = null;
        HttpResponse response = null;
        Header[] cookieHeaders = null;
        try {
            response = httpclient.execute(httpGet);
            cookieHeaders = response.getHeaders("Set-Cookie");
            //释放连接
            EntityUtils.consume(response.getEntity());

            // 设置登录参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("username", "18638217959"));
            formparams.add(new BasicNameValuePair("pwd", "1qaz1qaz"));
            formparams.add(new BasicNameValuePair("target", "http://m.ule.com:80/user/center?source=index"));
            UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");

            // 新建登陆Http post请求
            HttpPost httppost = new HttpPost(LOGIN_URL);
            httppost.setEntity(entity1);
            for (Header cookieHeader : cookieHeaders) {
                httppost.addHeader(cookieHeader);
            }
            responsePost = httpclient.execute(httppost);
            return responsePost.getHeaders("Set-Cookie");
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        } finally {
            EntityUtils.consume(responsePost.getEntity());
        }
        return null;
    }

    public CloseableHttpClient getPoolHttpClient() {
        // 单线程跑是基本看不出配置了连接池的好处的，只有使用多线程爬取数据的时候，并且数据量越大效果越明显
        PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();
        conMgr.setMaxTotal(threadNumber); // 设置整个连接池最大连接数 根据自己的场景决定
        // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。

        conMgr.setDefaultMaxPerRoute(threadNumber);// （目前只有一个路由，因此让他等于最大值）
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(conMgr).build();
        return httpClient;
    }

    private void praseResult(String result, int addressId) {
        if(result.indexOf("系统繁忙")>0){
            RedisDaoTemplate.getRedisDaoTemplate().execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(Jedis jedis) {
                   jedis.sadd(REDIS_KEY_ADDRESSID_ERROR,addressId+"");
                    return null;
                }
            });
        }else{
            RedisDaoTemplate.getRedisDaoTemplate().execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(Jedis jedis) {
                    jedis.hset(REDIS_KEY_ADDRESSID_SUCCESS,addressId+"",result);
                    jedis.lpush(REDIS_KEY_ADDRESSID_SUCCESS_LIST,addressId+"");
                    return null;
                }
            });
        }
    }

}
