package com.ggj.encrypt.interceptor;

import com.ggj.encrypt.common.utils.SpringContextHolder;
import com.ggj.encrypt.security.LoginSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:gaoguangjin
 * @Description:自定义登陆拦截
 * @Email:335424093@qq.com
 * @Date 2016/4/26 15:04
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private LoginSecurity loginSecurity= SpringContextHolder.getBean(LoginSecurity.class);
    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");
    /**
     * 检验用户是否登陆，没有登录直接返回
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            long beginTime = System.currentTimeMillis();// 1、开始时间
            startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
        loginSecurity.checkIsLogin(request);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            long beginTime = startTimeThreadLocal.get();
            long endTime = System.currentTimeMillis();
            long execTime = endTime-beginTime;
        //大于5s,打印一下警告日志
        if (execTime > 5000) {
            log.info(request.getRequestURI() + " 访问时间过长, 耗费:" + execTime + " ms");
        }
    }
}
