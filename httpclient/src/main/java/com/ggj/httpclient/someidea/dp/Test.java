package com.ggj.httpclient.someidea.dp;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * @author:gaoguangjin
 * @date:2018/8/24
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws IOException {
        Content dd = Request.Post("http://s.dianping.com/ajax/json/activity/offline/followNoteAdd")
                .addHeader("Cookie", "isChecked=checked; JSESSIONID=5F9986BAAD08E91A40A18F9B16207DF0; _hc.v=7ff280b3-a676-c8dc-1a84-e5c9067f4dce.1534824829; _lxsdk_cuid=1655ab04f9ac8-00e38d0412b1cf-34677909-1fa400-1655ab04f9bc8; _lxsdk=1655ab04f9ac8-00e38d0412b1cf-34677909-1fa400-1655ab04f9bc8; cityid=1; citypinyin=shanghai; cityname=5LiK5rW3; s_ViewType=10; Hm_lvt_4c4fc10949f0d691f3a2cc4ca5065397=1534137110,1534904349; m_flash2=1; dper=d0a06116fb82790b7f4b503d4d3819545532d655172fbcce35647455fd2970d565f1c804897a94a679d5f9ea51178e6a77bdd3aa929cc272531255fda8f4c9869554ee3733589bd55adf4a5b7281ab234461678ea971200d578e18f7aef57fc2; ll=7fd06e815b796be3df069dec7836c3df; ua=18638217959; Hm_lpvt_4c4fc10949f0d691f3a2cc4ca5065397=1534904979; edper=0a7c1b58f873e7f2d6b8cc9821f469e2b7bb79faed2ed581c1592876d7643944; aburl=1; cye=dplab; ta.uuid=1032881519540281362; isUuidUnion=true; iuuid=1655ab04f9ac8-00e38d0412b1cf-34677909-1fa400-1655ab04f9bc8; dpUserId=21304984; mtUserId=\"\"; pvhistory=\"6L+U5ZuePjo8L3N0YXRpY3Rlc3QvbG9nZXZlbnQ/bmFtZT1XaGVyZUFtSUZhaWwmaW5mbz1odG1sLSU1QiU3QiUyMmNvZGUlMjIlM0EzJTJDJTIybWVzc2FnZSUyMiUzQSUyMlRpbWVvdXQlMjBleHBpcmVkJTIyJTdEJTVEJmNhbGxiYWNrPVdoZXJlQW1JMTE1MzUwOTMxMjk2OTI+OjwxNTM1MDkzMTI5NzU5XV9b\"; ri=1000310100; m_set_info=%7B%22ri%22%3A%221000310100%22%2C%22rv%22%3A%221535093140885%22%2C%22ui%22%3A%226081504%22%7D; rv=1535093140885; cy=1; _lx_utm=utm_source%3Ddp_pc_event; _lxsdk_s=1656aecd48d-b4d-6d4-d8d%7C%7C96")
                .bodyForm(Form.form().add("offlineActivityId", "504460353").add("noteBody", "ddd").build())
                .execute().returnContent();
        log.info(dd.asString());
    }
}
