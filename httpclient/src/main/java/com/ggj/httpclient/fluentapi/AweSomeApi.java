package com.ggj.httpclient.fluentapi;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.jndi.toolkit.url.UrlUtil;
import com.sun.source.doctree.SinceTree;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author:gaoguangjin
 * @Description: http://blog.csdn.net/vector_yi/article/details/24298629
 * @Email:335424093@qq.com
 * @Date 2016/4/11 13:40
 */
@Slf4j
public class AweSomeApi {

    public static void main(String[] args) throws Exception {

        while (true){

            try {

                String result = Request.Post("http://localhost:9999/admin/api/upsTrack/v3").bodyString("{\n" +
                //String result = Request.Post("http://localhost:9627/api/upsTrack/v3").bodyString("{\n" +
                        "\t\"trackNumberList\":[\"9205590265114231946727\"]\n" +
                        "}", ContentType.getByMimeType("application/json")).execute().returnContent().asString();
                log.info(result);
                Thread.sleep(100);
            }catch (Exception e){
                log.error("error la");
            }
        }



        //get();
       /* ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(1000);
        for (int i = 0; i <1000 ; i++) {

            threadPoolExecutor.execute(new Thread() {
                @Override
                public void run() {
                    get();
                }
            });

        }*/
    }

    private static void post2() {
        //http://s.dianping.com/ajax/json/activity/offline/followNoteAdd
        try {
            String result = Request.Post("http://s.dianping.com/ajax/json/activity/offline/followNoteAdd").addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cookie", "_lxsdk_cuid=16d0082d940c8-080872a1613d3-38657501-13c680-16d0082d941c8; _lxsdk=16d0082d940c8-080872a1613d3-38657501-13c680-16d0082d941c8; _hc.v=a451e489-86be-a2cf-ad6d-af35d339e48e.1567671639; ctu=9608e1fb7e43b29ded2fadd1385a89542e1efc7edc3c8a4d9c70843ac8204269; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; cy=2; cye=beijing; lgtoken=0c20a87e1-1522-4867-b171-a0e7cf524d6f; dper=7db75ec8219a16bc50a6490c1e9854fba8cbc4f284d803a4427eaf598292e7327c5117887848cd9b3a9b4b03c97fe7f9c7217e7a40e8f540f4b95339e9a019ee8b6bc8f88d7663585c92dbca1b12364e58f0229fdccd8d184e6105b886dea39c; ll=7fd06e815b796be3df069dec7836c3df; ua=%E4%B8%80%E4%B8%AA%E6%A2%A6%E6%83%B3%E6%88%90%E4%B8%BA%E5%A5%B3%E7%8E%8B%E7%9A%84%E4%B9%9E%E4%B8%90%E3%80%82; uamo=18601693171; _lxsdk_s=16d00c885ab-fb0-f68-e02%7C%7C151")
                    .bodyForm(Form.form().add("offlineActivityId", "559809280").add("toUserId", "0")
                            .add("noteBody", UrlUtil.encode("国际在线报道（记者 阮佳闻）：应国务院总理李克强邀请，德国总理默克尔将于6日至7日对中国进行正式访问。中国驻德大使吴恳当地时间4日在德国《每日镜报》上发表题为《继往开来，深化合作》的署名文章，回顾了中德关系近年来高水平发展的亮点。\n" +
                                    "\n" +
                                    "吴恳在文章中说，作为访华次数最多的西方国家领导人，默克尔总理即将开启的第12次访华之旅欣逢新中国成立70周年之际，令人想到中德关系中还有很多个“最”：中德政府磋商是中国同西方主要国家之间级别最高且唯一由两国总理主持、双方部长参加的政府磋商机制；中国已连续三年成为德国最大贸易伙伴，2019年上半年双边贸易额约1000亿欧元，占中欧贸易额的三分之一。", "utf-8"))

                            .build()).execute().returnContent().asString();
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 23:29:22.318 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1071
     * 23:29:25.992 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:667
     * 23:29:29.753 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:758
     * 23:29:33.957 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1199
     * 23:29:37.772 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:815
     * 23:29:44.731 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:3955
     * 23:29:48.518 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:782
     * 23:29:54.857 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:3334
     * 23:29:59.243 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1384
     * 23:30:05.107 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:2862
     * 23:30:13.797 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:5688
     * 23:30:19.956 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:3158
     * 23:30:24.277 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1320
     * 23:30:27.961 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:683
     * 23:30:41.971 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:11008
     * 23:30:48.818 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:3847
     * 23:30:52.560 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:741
     * 23:30:56.382 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:822
     * 23:31:00.903 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1517
     * 23:31:05.064 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1159
     * 23:31:08.768 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:701
     * 23:31:12.487 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:716
     * 23:31:17.437 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1946
     * 23:31:21.600 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1160
     * 23:31:28.799 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:4195
     * 23:31:37.467 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:5663
     * 23:31:42.508 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:2036
     * 23:31:46.222 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:711
     * 23:31:51.170 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1944
     * 23:31:55.084 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:910
     * 23:31:59.594 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:1505
     * 23:32:04.714 [main] INFO  c.g.httpclient.fluentapi.AweSomeApi - cost:2116
     */
    //The same requests can be executed using a simpler, albeit less flexible, fluent API.
    private static void get() {
        try {


             WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

            webClient.getOptions().setThrowExceptionOnScriptError(true);//当JS执行出错的时候是否抛出异常, 这里选择不需要
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
            webClient.getOptions().setActiveXNative(false);
            webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
            webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

            HtmlPage page = null;
            try {
                page = webClient.getPage("http://m.usps.com/m/TrackConfirmAction_detail?tLabels=4208383792748927005303010099626059");//尝试加载上面图片例子给出的网页
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                webClient.close();
            }

            webClient.waitForBackgroundJavaScript(30000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

            String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串

            //TODO 下面的代码就是对字符串的操作了,常规的爬虫操作,用到了比较好用的Jsoup库

            Document document = Jsoup.parse(pageXml);//获取html文档
            List<Element> infoListEle = document.getElementById("feedCardContent").getElementsByAttributeValue("class", "feed-card-item");//获取元素节点等





            // Thread.sleep(3000);
          //  Long beginTime = System.currentTimeMillis();
            ;
            log.info("");
          //  log.info("cost:{},{}", request.execute().returnContent().asString(),(System.currentTimeMillis() - beginTime));
        } catch (Exception e) {
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
                .bodyForm(Form.form().add("username", "vip").add("password", "secret").build())
                .execute().returnContent();


    }


}
