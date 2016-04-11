package com.ggj.httpclient.someidea;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author:gaoguangjin
 * @Description: 最开始的版本
 * @Email:335424093@qq.com
 * @Date 2016/4/7 1:16
 */
@Slf4j
public class HttpLogin {
	private static final String LOGIN_URL = "http://m.ule.com:80/user/login";
	private static final String INDEX_URL = "http://m.ule.com/";
	private static final String ADDRESS_URL = "http://m.ule.com/address/detail/";
    private List<Integer> successLinkedList=new LinkedList<Integer>();
	
	public static void main(String[] args) {
		HttpLogin hl = new HttpLogin();
		
		hl.login();
	}
	
	public void login() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(INDEX_URL);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			Header[] cookieHeaders = response.getHeaders("Set-Cookie");
			
			// 设置登录参数
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("username", "18638217959"));
			formparams.add(new BasicNameValuePair("pwd", "1qaz1qaz"));
			formparams.add(new BasicNameValuePair("target", "http://m.ule.com:80/user/center?source=index"));
			UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
			// 新建Http post请求
			
			HttpPost httppost = new HttpPost(LOGIN_URL);
			httppost.setEntity(entity1);
			for (Header cookieHeader : cookieHeaders) {
				httppost.addHeader(cookieHeader);
				// log.info(cookieHeader.getName() + "=" + cookieHeader.getValue());
			}
			
			EntityUtils.consume(response.getEntity());
			HttpResponse responsePost = httpclient.execute(httppost);
			Header[] loginCookieHeaders = responsePost.getHeaders("Set-Cookie");
			
			EntityUtils.consume(responsePost.getEntity());

            //带着cookie去访问
//			HttpGet httpGetAddress = new HttpGet(ADDRESS_URL);
//			for (Header cookieHeader : loginCookieHeaders) {
//				// log.info(cookieHeader.getName() + "=" + cookieHeader.getValue());
//				httpGetAddress.addHeader(cookieHeader);
//			}
//			HttpResponse responseAddress = httpclient.execute(httpGetAddress);
//			String result = EntityUtils.toString(responseAddress.getEntity(), "UTF-8");
//			log.info(result);

            for (int i=0;i<=2560000;i++)
             praseAddress(httpclient,loginCookieHeaders,i);

            log.info(""+successLinkedList.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    //需要公用一个httpclient 不然上下文不一致,串行的速度太慢了。
    private void praseAddress(CloseableHttpClient httpclient, Header[] loginCookieHeaders, int addressId) {
        try {
            HttpGet httpGetAddress = new HttpGet(ADDRESS_URL+addressId);
            for (Header cookieHeader : loginCookieHeaders) {
                httpGetAddress.addHeader(cookieHeader);
            }
            HttpResponse responseAddress = httpclient.execute(httpGetAddress);
            String result = EntityUtils.toString(responseAddress.getEntity(), "UTF-8");
            praseResult(result,addressId);
            EntityUtils.consume(responseAddress.getEntity());
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void praseResult(String result, int addressId) {
       if(result.indexOf("系统繁忙")>0){
          // log.info("错误的地址");
       }else{
           successLinkedList.add(addressId);
           log.info("正确的地址"+addressId);
       }
    }

}
