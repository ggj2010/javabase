
package com.ggj.webmagic.cat;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author:gaoguangjin
 * @date:2018/11/21
 */
@Getter
@Setter
public class ResultBean {
    private String ma_title;
    private String ma_text;
    private String mu_email;
    private int mu_id;
    private List<String> ma_images;
    private String mv_play_url;
    private String mv_like;
    private String mv_comment;
    // 2018-11-20 02:58:57
    private String mv_created;
    private String mu_name;
}
