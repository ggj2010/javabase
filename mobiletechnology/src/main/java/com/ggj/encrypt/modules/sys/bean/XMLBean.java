package com.ggj.encrypt.modules.sys.bean;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:gaoguangjin
 * @Description: 返回xml格式
 * @Email:335424093@qq.com
 * @Date 2016/3/30 10:48
 */
@Getter
@Setter
@XmlRootElement(name = "xmlTest")
public class XMLBean {
    private String id;
    private String name;
    public XMLBean(){

    }
    public XMLBean(String id, String name) {
        this.id=id;
        this.name=name;
    }
}
