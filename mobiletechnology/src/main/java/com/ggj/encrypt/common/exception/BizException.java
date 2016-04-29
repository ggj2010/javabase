package com.ggj.encrypt.common.exception;


import com.ggj.encrypt.common.persistence.Result;

/**
 * @author:gaoguangjin
 * @Description: 封装业务异常
 * @Email:335424093@qq.com
 * @Date 2016/4/26 16:16
 */
public class BizException extends  Exception {

    private static final long serialVersionUID = 1L;

    private String code;

    public BizException(String message) {
        super(message);
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 异常返回封装对象
     * @return
     */
    public Result getReturnRestult() {
        return new Result(this.getCode(), this.getMessage());
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
