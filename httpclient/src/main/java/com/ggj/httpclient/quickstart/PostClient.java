package com.ggj.httpclient.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/1 17:53
 */
@Slf4j
public class PostClient {
	String url = "http://www.tuling123.com/openapi/api";
	
	public void post() throws IOException {
		HttpResponse response = null;
		HttpEntity entity = null;
		CloseableHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(url);
			// 参数：
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("key", "4b441cb500f431adc6cc0cb650b4a5d0"));
			params.add(new BasicNameValuePair("info", "上海去嘉兴火车"));
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(urlEncodedFormEntity);
			
			// 执行pos
			response = httpclient.execute(post);
			log.info("status:" + response.getStatusLine());
			if (response.getStatusLine().getStatusCode() == 200) {
				entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				log.info(result);
			}
		} catch (Exception e) {
			// 处理异常
			log.error("" + e.getLocalizedMessage());
		} finally {
			if (response != null) {
				EntityUtils.consume(entity); // 会自动释放连接
			}
		}
	}

	/**
	 * 参数放到body 里面
	 * @throws IOException
     */
	public void post2() throws IOException {
		HttpResponse response = null;
		HttpEntity entity = null;
		CloseableHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(url);
			// 参数：
			String params="";
			StringEntity se = new StringEntity(params, "utf-8");
			post.setEntity(se);
			// 执行pos
			response = httpclient.execute(post);
			log.info("status:" + response.getStatusLine());
			if (response.getStatusLine().getStatusCode() == 200) {
				entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				log.info(result);
			}
		} catch (Exception e) {
			// 处理异常
			log.error("" + e.getLocalizedMessage());
		} finally {
			if (response != null) {
				EntityUtils.consume(entity); // 会自动释放连接
			}
		}
	}

	/**
	 * 请求gzip 压缩  服务器返回的信息是压缩过的
	 * Cache-Control:private
	 Connection:keep-alive
	 Content-Encoding:gzip
	 Content-Type:text/html; charset=utf-8
	 Date:Wed, 06 Apr 2016 08:11:47 GMT
	 Expires:Thu, 19 Nov 1981 08:52:00 GMT
	 Pragma:no-cache
	 Server:nginx
	 Transfer-Encoding:chunked
	 Vary:Accept-Encoding
	 X-Powered-By:ThinkPHP
	 X-Powered-By-360WZB:wangzhan.360.cn
	 * DefaultHttpClient 使用GZIPInputStream解压缩 当浏览器访问网站时，有可能浏览器返回的消息头中带有 Content-Encoding:gzip，
	 * 表明服务器返回的消息经过gzip压缩，这么做是为了节省流量，浏览器拿到gzip压缩后的http包，
	 * 对其进行解压缩，再渲染出来。在使用apache提供的 DefaultHttpClient操作http请求时，
	 * 可以使用 GZIPInputStream对gzip压缩过的数据包进行解压缩。android sdk进行网络编程时，也可以使用这种方法。简单代码如下：
	 * @throws IOException
	 */
	public void gzip() throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://ce.cloud.360.cn/task");
		post.addHeader("Referer", "http://ce.cloud.360.cn/task");
		post.addHeader("Host", "ce.cloud.360.cn");
		post.addHeader("Origin", "http://ce.cloud.360.cn");
		post.addHeader("X-Requested-With", "XMLHttpRequest");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		// gzip 压缩
		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Accept-Language", "zh-cn,en-us;q=0.7,en;q=0.3");
		post.addHeader("Pragma", "no-cache");
		post.addHeader("Cache-Control", "no-cache");
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		
		params.add(new BasicNameValuePair("type", "get"));
		params.add(new BasicNameValuePair("domain", "http://ggjlovezjy.date"));
		params.add(new BasicNameValuePair("_token__", "15a2028adadc8fa72c59a1098ed24e7b_f98a473a07c18b1b6aaf2670bbf66bb1"));
		UrlEncodedFormEntity formEntity = null;
		HttpResponse response = null;
		String responseHtml = null;
		HttpEntity entity = null;
		try {
			formEntity = new UrlEncodedFormEntity(params, "utf-8");
			post.setEntity(formEntity);
			
			response = httpClient.execute(post);
			entity = response.getEntity();
			InputStream is = entity.getContent();
			is = new GZIPInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			responseHtml = sb.toString();
			log.info(responseHtml);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				EntityUtils.consume(entity); // 会自动释放连接
			}
		}
	}
}
