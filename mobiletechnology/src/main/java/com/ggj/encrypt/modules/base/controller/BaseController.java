package com.ggj.encrypt.modules.base.controller;

import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.persistence.Result;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/26 16:20
 */
@Controller
public class BaseController implements InitializingBean {
    @Autowired
    private ResultCodeConfiguration resultCodeConfiguration;
    private String exceptionJson="";
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @ExceptionHandler({ BizException.class })
    public @ResponseBody
    String bizException(BizException e, HttpServletRequest request) {
        logger.error("BizException异常："+e.getLocalizedMessage());
        return e.getReturnRestult().toJSONString();
    }
    @ExceptionHandler({ Exception.class })
    public @ResponseBody String exception(Exception e, HttpServletRequest request) {
        logger.error("Exception异常："+e.getMessage());
        return exceptionJson;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionJson=new Result(resultCodeConfiguration.getErrCode(), resultCodeConfiguration.getErrMsg()).toJSONString();
    }
}
