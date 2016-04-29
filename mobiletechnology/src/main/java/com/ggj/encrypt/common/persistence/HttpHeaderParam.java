package com.ggj.encrypt.common.persistence;


import com.ggj.encrypt.common.constant.GlobalConstant;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;


/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/25 13:59
 */
@Getter
@Setter
public class HttpHeaderParam {
    //签名串
    private String sign;
    private String userId;
    private String appkey;
    private String timeStamp;

    public String toString(){
        StringBuilder instance = new StringBuilder();
        instance.append(" HttpHeader: {");
        instance.append(GlobalConstant.TIMESTAMP + "[").append(this.timeStamp).append("], ");
        instance.append(GlobalConstant.SIGN + "[").append(this.sign).append("] ");
        instance.append(GlobalConstant.USE_ID + "[").append(this.userId).append("] ");
        instance.append(GlobalConstant.APPKEY + "[").append(this.appkey).append("] ");
        instance.append(" }");
        return instance.toString();
    }

    /**
     * 解析HttpServletRequest，生成HttpHeader对象
     * @param request
     * @return
     */
    public static HttpHeaderParam parseRequestHeader(HttpServletRequest request) {
        HttpHeaderParam header = new HttpHeaderParam();
        header.setTimeStamp(request.getHeader(GlobalConstant.TIMESTAMP));
        header.setSign(request.getHeader(GlobalConstant.SIGN));
        header.setUserId(request.getHeader(GlobalConstant.USE_ID));
        return header;
    }

}
