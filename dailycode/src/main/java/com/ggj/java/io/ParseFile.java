package com.ggj.java.io;

import com.ggj.java.qiniu.speciall.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ParseFile {
    private final static String FILE_PATH = "/Users/gaoguangjin/Downloads/res_text.txt";

    public static void main(String[] args) throws Exception {
        Set<String> chineseSet = new HashSet<>();
        Set<String> engSet = new HashSet<>();
        try {
            List<String> listString = IOUtils.readLines(new FileInputStream(new File(FILE_PATH)));
            for (String content : listString) {
                String originContent = content;
                if (StringUtils.isEmpty(content)) {
                    continue;
                }
                //去除富文本
                content = content.replaceAll("<[^>]+>", " ").replaceAll("&nbsp;", " ");

                String chContent = content;
                String enContent = content;
                //每条数据都有中文和英语
                //处理中文
                chContent = chContent.replaceAll("[^\\u4E00-\\u9FA5 ]", "").replaceAll(" ", "");
                for (int i = 0; i < chContent.length(); i++) {
                    chineseSet.add(chContent.substring(i, i + 1));

                }
                //log.info("中文：{}",chContent);
                //处理英语
                enContent = enContent.replaceAll("[^a-zA-Z]", " ");
                String[] engArraycontent = enContent.split(" ");
                if (engArraycontent.length == 0) {
                    continue;
                }
                for (String str : engArraycontent) {
                    if (StringUtils.isEmpty(str)) {
                        continue;
                    }
                    engSet.add(str);
                }
                //log.info("英文：{}",enContent);
            }

            String chineseStr = chineseSet.stream().collect(Collectors.joining(" "));
            String endStr = engSet.stream().collect(Collectors.joining(" "));
            FileUtils.write(new File("/Users/gaoguangjin/Downloads/中文.txt"), chineseStr);
            FileUtils.write(new File("/Users/gaoguangjin/Downloads/英语.txt"), endStr);
        } catch (Exception e) {
            log.error("e", e);
        }
    }
}
