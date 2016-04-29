package com.ggj.encrypt.modules.sys.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/27 18:06
 */
@Getter
@Setter
public class UserInfo implements Serializable{
    private static final long serialVersionUID = -1950504748764500854L;
    private Long id;
    private String loginName;
    private String password;
    //....

    public String toString(){
        return "id="+id+";loginName="+loginName+";password="+password;
    }
}
