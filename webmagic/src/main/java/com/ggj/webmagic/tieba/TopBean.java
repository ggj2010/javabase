package com.ggj.webmagic.tieba;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author:gaoguangjin
 * @date 2016/8/18 22:14
 */
@Getter
@Setter
public class TopBean {
    private String index;
    private String userName;
    private String level;
    private String experience;
    private String image;
    private Date createDate;
    private String tieBaName;

    public TopBean(String index,String userName, String level, String experience,String tieBaName) {
        this.index=index;
        this.userName=userName;
        this.level=level;
        this.createDate=new Date();
        this.experience=experience;
        this.tieBaName=tieBaName;
    }

    public TopBean() {
    }
}
