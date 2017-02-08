package com.ggj.httpclient.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:gaoguangjin
 * @Description:Http连接是复杂，有状态的，线程不安全的对象，所以它必须被妥善管理。一个Http连接在同一时间只能被一个线程访问
 * http://www.yeetrack.com/?p=782
 * @Email:335424093@qq.com
 * @Date 2016/4/6 17:25
 */
@Slf4j
public class HttpclientManager {

	AtomicInteger errorAtomicInteger=new AtomicInteger();
	
	private String url = "http://localhost:8080/index";
	//外网的域名网速不稳定
//	private String url = "http://www.baidu.com";
	private int threadNumber = 1000;

    // 多线程httpclient解决超时连接,  设置请求超时2秒钟 根据业务调整 ;设置等待数据超时时间2秒钟 根据业务调整;该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大 ()
    RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(2000).setConnectionRequestTimeout(500)
            .setStaleConnectionCheckEnabled(true).build();

    /**
	 * httpclientManager.threadPoolHttp();效率高于httpclientManager.wrongThreadHttp();
	 * 测试访问本地 http://localhost:8080/index 1000线程
	 01:10:42.877 [main] INFO  c.g.h.quickstart.HttpclientManager - 执行wrongThreadHttp时间：7335
	 01:10:42.877 [main] INFO  c.g.h.quickstart.HttpclientManager - connect time out 错误个数：0
	 *
	 01:11:19.443 [main] INFO  c.g.h.quickstart.HttpclientManager - 执行threadPoolHttp时间：5296
	 01:11:19.443 [main] INFO  c.g.h.quickstart.HttpclientManager - connect time out 错误个数：0
     * @param args
     */
	public static void main(String[] args) {
		HttpclientManager httpclientManager = new HttpclientManager();
//		httpclientManager.wrongThreadHttp();
        //请求连接池管理器PoolingClientConnectionManager
        httpclientManager.threadPoolHttp();
	}
	
	/**
	 *   CloseableHttpClient httpclient = HttpClients.createDefault();
	 CloseableHttpClient httpclientS = new DefaultHttpClient();
	 虽然我们是多线程的写法，但是在httpclient.execute(httGet);执行的时候是加锁串行的，也就是真正访问到服务器端那边是没有并发的
	 请求是一个一个过来的，这样很影响效率！！！！
	 */
	public void wrongThreadHttp() {
		long beingTime = System.currentTimeMillis();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
		for (int i = 0; i < threadNumber; i++) {
            //串行执行效果
//			executorService.execute(getThread(httpclient));
			//并发执行效果
            executorService.execute(getThread(null));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			long endTime = System.currentTimeMillis();
			log.info("执行wrongThreadHttp时间：" + (endTime - beingTime));
			log.info("connect time out 错误个数：" +errorAtomicInteger.getAndIncrement());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void threadPoolHttp() {
		long beingTime = System.currentTimeMillis();
		CloseableHttpClient httpclient = getPoolHttpClient();
		ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
		for (int i = 0; i < threadNumber; i++) {
			executorService.execute(getThread(httpclient));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			long endTime = System.currentTimeMillis();
			log.info("执行threadPoolHttp时间：" + (endTime - beingTime));
			log.info("connect time out 错误个数：" +errorAtomicInteger.getAndIncrement());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private Thread getThread(CloseableHttpClient httpclient) {
		return new Thread(() -> {
			HttpGet httGet = new HttpGet(url);
            //设置访问参数
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).build();
            httGet.setConfig(defaultRequestConfig);
			CloseableHttpResponse resopnse = null;
			HttpEntity entity = null;
			try {
				log.info("开始请求");
				// 每个线程独立一个httpclient才可以达到线程的效果
				if (httpclient == null) {
					resopnse = HttpClients.createDefault().execute(httGet);
				} else {
					// 多线程公用一个httpclient会导致串行,一个Http连接在同一时间只能被一个线程访问
					resopnse = httpclient.execute(httGet);
				}
				log.info("status:" + resopnse.getStatusLine());
				// 休眠模拟多个线程抢占httpclient
				entity = resopnse.getEntity();
				String result = EntityUtils.toString(entity, "utf-8");
				// log.info(result);
			} catch (IOException e) {
				errorAtomicInteger.getAndIncrement();
				log.error("" + e.getLocalizedMessage());
			} finally {
				if (resopnse != null) {
					try {
						EntityUtils.consume(entity); // 会自动释放连接
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * 当使用了请求连接池管理器（比如PoolingClientConnectionManager）后，HttpClient就可以同时执行多个线程的请求了。
	 * @return
	 */
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
}
