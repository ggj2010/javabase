package com.ggj.webmagic.tieba.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TieBaImage {
    private Integer id;
    private String pageId;
    private String imageUrl;
    private String tiebaName;

    public TieBaImage(String pageId, String imageUrl, String tiebaName){
        this.pageId=pageId;
        this.imageUrl=imageUrl;
        this.tiebaName=tiebaName;
    }
}