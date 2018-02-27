package com.ggj.httpclient;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date:2018/1/29
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        try {
            for (int i = 0; i <= 6; i++) {
                JSONObject dd = JSONObject.parseObject("{tmpImg://p0.meituan.net/dprainbow/09743d7deedec9ca77dc71cb20bc79ac105018.png,type:[门店宣传],publishEditUser:zhongwenwen,unoDpAppPreviewEditUrl:https://g.dianping.com/app/uno/edit/1490089/index.html,unoDpZeusPreviewEditUrl:https://g.dianping.com/app/uno/edit/1490089/dpzeus.html}");

            }
        } catch (Exception e) {
             System.out.println(e.getLocalizedMessage());
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - beginTime);
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - beginTime);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - beginTime);
    }
}
