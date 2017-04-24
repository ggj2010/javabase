package com.ggj.httpclient.fluentapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author:gaoguangjin
 * @Description: http://blog.csdn.net/vector_yi/article/details/24298629
 * @Email:335424093@qq.com
 * @Date 2016/4/11 13:40
 */
@Slf4j
public class AweSomeApi {

    public static void main(String[] args) {
        get();
    }

    //The same requests can be executed using a simpler, albeit less flexible, fluent API.
    private static void get() {
        try {
            String result = Request.Get("https://www.baidu.com/").execute().returnContent().asString(Charset.forName("utf-8"));
            log.info(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void post() throws IOException {
        //以Http/1.1版本协议执行一个POST请求，同时配置Expect-continue handshake达到性能调优,
        //请求中包含String类型的请求体并将响应内容作为byte[]返回
        Request.Post("http://blog.csdn.net/vector_yi")
                .useExpectContinue()
                .version(HttpVersion.HTTP_1_1)
                .bodyString("Important stuff", ContentType.DEFAULT_TEXT)
                .execute().returnContent().asBytes();


        //通过代理执行一个POST请求并添加一个自定义的头部属性,请求包含一个HTML表单类型的请求体
        //将返回的响应内容存入文件
        Request.Post("http://blog.csdn.net/vector_yi")
                .addHeader("X-Custom-header", "stuff")
                .viaProxy(new HttpHost("myproxy", 8080))
                .bodyForm(Form.form().add("username", "vip").add("password", "secret").build())
                .execute().saveContent(new File("result.dump"));

        Request.Post("http://targethost/login")
                .bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
                .execute().returnContent();


    }


}
