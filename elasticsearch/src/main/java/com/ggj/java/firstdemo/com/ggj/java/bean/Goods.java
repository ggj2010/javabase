package com.ggj.java.firstdemo.com.ggj.java.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 分词
 * @author:gaoguangjin
 * @date 2016/9/27 21:47
 */
@Getter
@Setter
public class Goods {
    private Integer id;
    private String title;
    private String url;
    public Goods(Integer id,String title,String url){
        this.id=id;
        this.url=url;
        this.title=title;
    }
}
