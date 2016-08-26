package com.ggj.webmagic.tieba.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * teiba 内容
 * @author:gaoguangjin
 * @date 2016/8/26 17:15
 */
@Getter
@Setter
public class ContentBean {
    //贴吧id
    private String id;
    //贴吧名称
    private String name;
    //最后更新时间
    private String  date;

    public ContentBean(String pageId, String date, String tiebaName) {
        id=pageId;
        this.date=date;
        name=tiebaName;
    }
    public ContentBean(String pageId,  String tiebaName) {
        id=pageId;
        name=tiebaName;
    }
    public ContentBean() {
    }
}
