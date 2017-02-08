package com.ggj.java.firstdemo.com.ggj.java.bean;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentType.JSON;

/**
 * @author:gaoguangjin
 * @date 2016/11/14 10:24
 */
@Getter
@Setter
public class ItemInfo {
    private Integer id;
    private String name;
    private float price;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;


    public ItemInfo(Integer id,String name,float price,Date createDate){
        this.id=id;
        this.name=name;
        this.price=price;
        this.createDate=createDate;
    }
}
