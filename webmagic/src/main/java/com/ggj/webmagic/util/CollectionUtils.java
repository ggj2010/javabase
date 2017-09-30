package com.ggj.webmagic.util;

import com.ggj.webmagic.WebmagicService;

import java.util.*;

/**
 * @author:gaoguangjin
 * @date 2016/8/26 20:29
 */
public class CollectionUtils {

    public static Set<String> convertSetByteToSetString(Set<byte[]> bytes) {
        Set<String> set = new HashSet<>();
        if (bytes != null) {
            for (byte[] by : bytes) {
                set.add(WebmagicService.getString(by));
            }
        }
        return set;
    }


    public static Set<String> convertMapByteToSetString(Map<byte[], byte[]> map) {
        Set<String> set = new HashSet<>();
        if (map != null) {
            for (byte[] bytes : map.keySet()) {
                set.add(WebmagicService.getString(bytes));
            }
        }
        return set;
    }

    public static List<String> converSetToList(Set<byte[]> sortPageIds) {
        List<String> list = new ArrayList<>();
        for (byte[] bytes : sortPageIds) {
            list.add(WebmagicService.getString(bytes));
        }
        return list;
    }
}
