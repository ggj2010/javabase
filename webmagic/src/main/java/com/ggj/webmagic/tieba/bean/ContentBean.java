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
    //帖子作者名称
    private String authorName;
    //帖子标题
    private String title;
    //最后更新时间
    private String  date;

    public ContentBean(String pageId, String date, String tiebaName,String authorName,String title) {
        this.id=pageId;
        this.date=date;
        this.name=tiebaName;
        this.authorName=authorName;
        this.title=title;
    }
    public ContentBean(String pageId,  String tiebaName) {
        this.id=pageId;
        this.name=tiebaName;
    }
    public ContentBean() {
    }
}
