package com.ggj.encrypt.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ggj.encrypt.common.persistence.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix ="resultCode")
public  class ResultCodeConfiguration  implements InitializingBean{

	private String successResultCode;
	private String errorResultCode;

	private String successCode;
	private String successMsg;
	private String errCode;
	private String errMsg;
	private String blackipCode;
	private String blackipMsg;
	private String IpMaxInvoke;
	private String IpMaxInvokeMsg;

	private String loginNameWrong;
	private String loginNameWrongMsg;
	private String passwordWrong;
	private String passwordWrongMsg;

	private String loginErrorLimit;
	private String loginErrorLimitMsg;

	private String paramErrorCode;
	private String paramErrorMsg;

	private String tokenErrorCode;
	private String tokenErrorCodeMsg;

	private String signatureErrorCode;
	private String signatureErrorCodeMsg;

	public String getSuccessResultCode(){
		return successResultCode;
	}
	public String getErrorResultCode(){
		return errorResultCode;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		successResultCode= JSON.toJSONString(new Result(successCode,successMsg));
		errorResultCode= JSON.toJSONString(new Result(errCode,errMsg));
	}

	public Result getResult(){
		return new Result(successCode,successMsg);
	}
}
