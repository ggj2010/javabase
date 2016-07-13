package com.ggj.encrypt.common.persistence;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/26 15:12
 */
@Getter
@Setter
public class Result implements Serializable {
    private static final long serialVersionUID = -7613802056292878981L;
    private String code;
	private String msg;
    private Object data;
    public Result(String code,String msg,Object data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public Result(String code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    /**
     *
     * @param data
     * @return
     */
    public String addData(Object data){
        this.setData(data);
        return this.toJSONString();
    }

    public String toJSONString(){
		return JSON.toJSONString(this);
    }
}
