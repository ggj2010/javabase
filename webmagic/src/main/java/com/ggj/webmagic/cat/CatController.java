
package com.ggj.webmagic.cat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.cat.util.AesEncryptionUtil;
import com.ggj.webmagic.cat.util.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author:gaoguangjin
 * @date:2018/11/26
 */
@Controller
@RequestMapping("/cat")
@Slf4j
public class CatController {
    public static final String EXTRA_KEY_UID = "uId";
    public static final String URL_SIG_KEY = "maomi_pass_xyz";
    private static String DOMAIN="";
    static {
        DOMAIN = System.getProperty("domain");
    }

    @RequestMapping("")
    public String index(Model model) throws Exception {
        model.addAttribute("data", listArticle(0));
        return "cat/index";
    }

    @RequestMapping("g")
    public String girl(Model model) throws Exception {
        model.addAttribute("data", listArticle(0));
        return "cat/qq";
    }

    @ResponseBody
    @RequestMapping("/page")
    public List<ResultBean> pageIndex(Integer page) throws Exception {
        log.info("page={}", page);
        return listArticle(page);
    }

    @ResponseBody
    @RequestMapping("/d")
    public String decryptMain(String encrptStr) throws Exception {
        return AesEncryptionUtil.decryptMain(encrptStr);
    }

    @ResponseBody
    @RequestMapping("/chart")
    public List<CharLog> chart(String userId) throws Exception {
        return getChartUrl(DOMAIN+"/api/chats/getChatUsers", userId);
    }

    private List<CharLog> getChartUrl(String url, String uId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(EXTRA_KEY_UID, uId);
        jsonObject.put("perPage", 20);
        jsonObject.put("page", 1);
        NameValuePair nameValuePair = new BasicNameValuePair("data",
                AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        Map map = new TreeMap();
        map.put("data", AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        NameValuePair nameValuePair2 = new BasicNameValuePair("sig", getParams(map));
        NameValuePair nameValuePair3 = new BasicNameValuePair("_device_id",
                "5A7146E3-E612-4C61-BB58-541EE145E7F6");
        NameValuePair nameValuePair4 = new BasicNameValuePair("_device_type", "iPhone7Plus");
        NameValuePair nameValuePair5 = new BasicNameValuePair("_device_version", "12.100000");
        NameValuePair nameValuePair6 = new BasicNameValuePair("_sdk_version", "2");
        NameValuePair nameValuePair7 = new BasicNameValuePair("_app_version", "1.0.2");
        List<CharLog> resultList = new ArrayList<>();
        try {
            String result = Request.Post(url)
                    .bodyForm(nameValuePair, nameValuePair2, nameValuePair3, nameValuePair4,
                            nameValuePair5, nameValuePair6, nameValuePair7)
                    .execute().returnContent().asString();
            // https://km.97kuaimao.com
            String jsonResult = AesEncryptionUtil.decryptMain(result);
            JSONArray jsonArray = JSONObject.parseObject(jsonResult).getJSONObject("data")
                    .getJSONArray("list");
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                List<CharLog> list = getChatlog(uId, j.getString("uid"));
                if (CollectionUtils.isNotEmpty(list)) {
                    resultList.addAll(list);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return resultList;
    }

    public List<CharLog> getChatlog(String uId, String fuId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(EXTRA_KEY_UID, uId);
        jsonObject.put("fuId", fuId);
        jsonObject.put("perPage", 20);
        jsonObject.put("page", 1);
        NameValuePair nameValuePair = new BasicNameValuePair("data",
                AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        Map map = new TreeMap();
        map.put("data", AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        NameValuePair nameValuePair2 = new BasicNameValuePair("sig", getParams(map));
        NameValuePair nameValuePair3 = new BasicNameValuePair("_device_id",
                "5A7146E3-E612-4C61-BB58-541EE145E7F6");
        NameValuePair nameValuePair4 = new BasicNameValuePair("_device_type", "iPhone7Plus");
        NameValuePair nameValuePair5 = new BasicNameValuePair("_device_version", "12.100000");
        NameValuePair nameValuePair6 = new BasicNameValuePair("_sdk_version", "2");
        NameValuePair nameValuePair7 = new BasicNameValuePair("_app_version", "1.0.2");
        try {
            String result = Request.Post(DOMAIN+"/api/chats/listOneChats")
                    .bodyForm(nameValuePair, nameValuePair2, nameValuePair3, nameValuePair4,
                            nameValuePair5, nameValuePair6, nameValuePair7)
                    .execute().returnContent().asString();
            String jsonResult = AesEncryptionUtil.decryptMain(result);
            JSONArray jsonArray = JSONObject.parseObject(jsonResult).getJSONObject("data")
                    .getJSONArray("list");
            if (jsonArray.size() >= 2) {
                return JSONArray.parseArray(jsonArray.toJSONString(), CharLog.class);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("/attention")
    public String pageIndex(String userId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        // jsonObject.put(EXTRA_KEY_UID, 24139206);
//        jsonObject.put(EXTRA_KEY_UID, 24253828);
        jsonObject.put(EXTRA_KEY_UID, 24405493);
        jsonObject.put("attentionId", userId);
        NameValuePair nameValuePair = new BasicNameValuePair("data",
                AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        Map map = new TreeMap();
        map.put("data", AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        NameValuePair nameValuePair2 = new BasicNameValuePair("sig", getParams(map));
        NameValuePair nameValuePair3 = new BasicNameValuePair("_device_id",
                "5A7146E3-E612-4C61-BB58-541EE145E7F6");
        NameValuePair nameValuePair4 = new BasicNameValuePair("_device_type", "iPhone7Plus");
        NameValuePair nameValuePair5 = new BasicNameValuePair("_device_version", "12.100000");
        NameValuePair nameValuePair6 = new BasicNameValuePair("_sdk_version", "2");
        NameValuePair nameValuePair7 = new BasicNameValuePair("_app_version", "1.0.2");
        try {
            String result = Request.Post(DOMAIN+"/api/community/attentionUser")
                    .bodyForm(nameValuePair, nameValuePair2, nameValuePair3, nameValuePair4,
                            nameValuePair5, nameValuePair6, nameValuePair7)
                    .execute().returnContent().asString();
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    private List<ResultBean> listArticle(int page) throws Exception {
        List<ResultBean> resultBeanList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(EXTRA_KEY_UID, 24139206);
        jsonObject.put("isGood", 0);
        jsonObject.put("isNew", 1);
        jsonObject.put("isHot", 0);
        jsonObject.put("mcId", 7);
        jsonObject.put("perPage", 10);
        jsonObject.put("page", page);
        NameValuePair nameValuePair = new BasicNameValuePair("data",
                AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        Map map = new TreeMap();
        map.put("data", AesEncryptionUtil.encryptMain(jsonObject.toJSONString()));
        NameValuePair nameValuePair2 = new BasicNameValuePair("sig", getParams(map));
        NameValuePair nameValuePair3 = new BasicNameValuePair("_device_id",
                "5A7146E3-E612-4C61-BB58-541EE145E7F6");
        NameValuePair nameValuePair4 = new BasicNameValuePair("_device_type", "iPhone7Plus");
        NameValuePair nameValuePair5 = new BasicNameValuePair("_device_version", "12.100000");
        NameValuePair nameValuePair6 = new BasicNameValuePair("_sdk_version", "2");
        NameValuePair nameValuePair7 = new BasicNameValuePair("_app_version", "1.0.2");
        try {
            String result = Request.Post(DOMAIN+"/api/community/listArticles")
                    .bodyForm(nameValuePair, nameValuePair2, nameValuePair3, nameValuePair4,
                            nameValuePair5, nameValuePair6, nameValuePair7)
                    .execute().returnContent().asString();
            // https://km.97kuaimao.com
            String jsonResult = AesEncryptionUtil.decryptMain(result).replaceAll("\\\\/", "/");
            JSONArray jsonArray = JSONObject.parseObject(jsonResult).getJSONObject("data")
                    .getJSONArray("list");
            for (Object o : jsonArray) {
                JSONObject j = (JSONObject) o;
                if (j.getString("mu_sex").equals("1")) {
                    resultBeanList.add(JSONObject.parseObject(j.toJSONString(), ResultBean.class));
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return resultBeanList;
    }

    public static String getParams(Map<String, String> params) throws Exception {
        params.put("_device_id", "5A7146E3-E612-4C61-BB58-541EE145E7F6");
        params.put("_app_version", "1.0.2");
        params.put("_device_type", "iPhone7Plus");
        params.put("_sdk_version", "2");
        params.put("_device_version", "12.100000");
        StringBuilder sb = new StringBuilder("");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("&").append((String) entry.getKey()).append("=")
                    .append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
        }
        return EncryptUtils.encryptMD5ToString(sb.toString().substring(1) + URL_SIG_KEY);
    }

}
