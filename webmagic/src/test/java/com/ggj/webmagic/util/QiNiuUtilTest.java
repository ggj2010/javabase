package com.ggj.webmagic.util;

import com.ggj.webmagic.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.AssertTrue;

import static org.apache.coyote.http11.Constants.a;
import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @date 2016/8/25 14:42
 */
public class QiNiuUtilTest   extends BaseTest {
    @Autowired
    private QiNiuUtil  qiNiuUtil;
    @Value("${qiniu.domain}")
    private String domain ;
    @Test
    public void upload() throws Exception {
        String key="sign=deeac03cc1ea15ce41eee00186013a25/ef943c6d55fbb2fb9ec7f6ab474a20a44723dc12.jpg";
        String url="http://imgsrc.baidu.com/forum/w%3D580/sign=deeac03cc1ea15ce41eee00186013a25/ef943c6d55fbb2fb9ec7f6ab474a20a44723dc12.jpg";
        String resultUrl=domain+key;
        Assert.assertEquals(resultUrl, qiNiuUtil.upload(key, url));
    }

}