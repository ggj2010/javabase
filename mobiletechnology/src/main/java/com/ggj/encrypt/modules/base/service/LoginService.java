package com.ggj.encrypt.modules.base.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ggj.encrypt.common.constant.GlobalConstant;
import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.persistence.ApiUserToken;
import com.ggj.encrypt.common.persistence.Result;
import com.ggj.encrypt.common.utils.DesUtil;
import com.ggj.encrypt.common.utils.TokenHelper;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/27 17:59
 */
@Service
public class LoginService {
    @Autowired
    private ResultCodeConfiguration resultCodeConfiguration;
    @Autowired
    private TokenHelper tokenHelper;
    private final  String LOGIN_NAME="loginName";
    private final  String PASSWORD="password";

    public UserInfo validateAndGetUserInfo(String p, String timestamp, String appkey) throws Exception {
        //解密
       String decryptParam=DesUtil.decrypt(p,appkey+timestamp);
        JSONObject userInfoJson= (JSONObject) JSON.parse(decryptParam);
        if(userInfoJson.containsKey(LOGIN_NAME)&&userInfoJson.containsKey(PASSWORD)){
            return JSON.parseObject(decryptParam,UserInfo.class);
        }else{
            //参数异常
            throw new BizException(resultCodeConfiguration.getParamErrorCode(), resultCodeConfiguration.getParamErrorMsg());
        }
    }

    /**
     * 登陆成功只返回token
     * @param dataBaseUserInfo
     * @param appkey
     * @param response
     * @return
     * @throws Exception
     */
    public String getSuccessLoginResult(UserInfo dataBaseUserInfo, String appkey, HttpServletResponse response) throws Exception {
        ApiUserToken token=tokenHelper.createToken(dataBaseUserInfo.getId(),appkey);
        //token加密后返回
        response.setHeader(GlobalConstant.USER_TONKEN,DesUtil.encrypt(token.getToken(),appkey));
        return resultCodeConfiguration.getSuccessResultCode();
    }
}
