
package com.ggj.webmagic.util;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.BaseTest;
import com.qiniu.common.Zone;
import com.qiniu.processing.OperationManager;
import com.qiniu.processing.OperationStatus;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @author:gaoguangjin
 * @date 2016/8/25 14:42
 */
@Slf4j
public class QiNiuUtilTest extends BaseTest {
    @Autowired
    private QiNiuUtil qiNiuUtil;
    @Value("${qiniu.domain}")
    private String domain;

    @Test
    public void upload() throws Exception {
        String key = "sign=deeac03cc1ea15ce41eee00186013a25/ef943c6d55fbb2fb9ec7f6ab474a20a44723dc12.jpg";
        String url = "http://imgsrc.baidu.com/forum/w%3D580/sign=deeac03cc1ea15ce41eee00186013a25/ef943c6d55fbb2fb9ec7f6ab474a20a44723dc12.jpg";
        String resultUrl = domain + key;
        Assert.assertEquals(resultUrl, qiNiuUtil.upload(key, url));
    }

    @Test
    public void test() throws Exception {
        qiNiuUtil.test();
    }

    public static void main(String[] args) {
        String bucket = "product";
        String key = "1539702607173729.mp4";
        String accessKey = "Bip6vCz50yIiMGjecwhvbRXluTP9XVWr8_NaA9Py";
        String secretKey = "f3KjUz5LUjbscU-FG-pyk_qkYTCyah25qS9WFmZW";
        // 待处理文件所在空间
        // 待处理文件名
        Auth auth = Auth.create(accessKey, secretKey);
        // 数据处理指令，支持多个指令
        String persistentOpfs = String.format("vframe/jpg/offset/0/w/480/h/360");
        // 数据处理队列名称，必须
        String persistentPipeline = "";
        // 数据处理完成结果通知地址
        String persistentNotifyUrl = "";
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        // ...其他参数参考类注释
        // 构建持久化数据处理对象
        OperationManager operationManager = new OperationManager(auth, cfg);
        try {
            String persistentId = operationManager.pfop(bucket,
                    URLEncoder.encode(key),
                    persistentOpfs,
                    UrlSafeBase64.encodeToString(persistentPipeline),
                    UrlSafeBase64.encodeToString(persistentNotifyUrl), true);
            // 可以根据该 persistentId 查询任务处理进度
            System.out.println(persistentId);
            OperationStatus operationStatus;
            while (true) {
                operationStatus = operationManager.prefop(persistentId);
                if (operationStatus.code == 0) {
                    break;
                }
            }
            // 解析 operationStatus 的结果
            log.info("视频缩略图地址：" + "http://images.3dclo.com/" + operationStatus.items[0].key);
            String result = Request.Get("http://images.3dclo.com/1539702607173729.mp4?avinfo")
                    .execute().returnContent().asString(Charset.forName("utf-8"));

            JSONObject jsonObject = JSONObject.parseObject(result);
            String duration = jsonObject.getJSONObject("format").getString("duration");
            log.info("视频长度：" + duration);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

}
