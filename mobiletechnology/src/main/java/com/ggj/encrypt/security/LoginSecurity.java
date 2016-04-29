package com.ggj.encrypt.security;

import com.ggj.encrypt.common.constant.GlobalConstant;
import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.utils.ApiTools;
import com.ggj.encrypt.common.utils.redis.RedisDaoTemplate;
import com.ggj.encrypt.common.utils.redis.callback.RedisCallback;
import com.ggj.encrypt.configuration.RedisKeyConfiguration;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.configuration.SystemConfiguration;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/26 18:41
 */
@Service
public class LoginSecurity {
    @Autowired
    private RedisDaoTemplate redisDaoTemplate;
    @Autowired
    private SystemConfiguration systemConfiguration;
    @Autowired
    private RedisKeyConfiguration redisKeyConfiguration;
    @Autowired
    private ResultCodeConfiguration resultCodeConfiguration;


    public Boolean checkIPAndInvokeNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //检查ip是否黑名单
        String ip = ApiTools.getIpAddr(request);
        return redisDaoTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(Jedis jedis) throws BizException {
                String key = redisKeyConfiguration.getBlackip();
                //是否启用黑名单功能，如果启用检查是否在黑名单里面
                if (systemConfiguration.getIsBlackipLimit().equals(GlobalConstant.TREU) && !jedis.sismember(key, ip)) {
                    //是否访问频繁
                    key = redisKeyConfiguration.getIpInvoke() + ip;
                    if (jedis.exists(key)) {
                        if (jedis.incr(key) > Long.parseLong(systemConfiguration.getIpMaxInvokeNumber()))
                            throw new BizException(resultCodeConfiguration.getIpMaxInvoke(), resultCodeConfiguration.getIpMaxInvokeMsg());
                    } else {
                        jedis.incr(key);
                        jedis.expire(key, 60);
                    }
                } else {
                    throw new BizException(resultCodeConfiguration.getBlackipCode(), resultCodeConfiguration.getBlackipMsg());
                }
                return true;
            }
        });
    }


    public void checkIsLogin(HttpServletRequest request) {

    }

    /**
     * 检查登陆错误次数
     * @param userInfo
     * @throws Exception
     */
    public void checkLoginErrorTimes(UserInfo userInfo) throws Exception {
        redisDaoTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(Jedis jedis) throws Exception {
                String key = redisKeyConfiguration.getLoginErrorLimit() + userInfo.getLoginName();
                String times = jedis.get(key);
                if(times!=null){
                    if(Integer.parseInt(times)>=Integer.parseInt(systemConfiguration.getLoginErrorMaxNumber())){
                        throw new BizException(resultCodeConfiguration.getLoginErrorLimit(), resultCodeConfiguration.getLoginErrorLimitMsg());
                    }
                }
                return null;
            }
        });
    }
    /**
     * 添加登陆错误次数
     * @param userInfo
     * @throws Exception
     */
    public void addLoginErrorTimes(UserInfo userInfo) throws Exception {
        redisDaoTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(Jedis jedis) throws Exception {
                String key = redisKeyConfiguration.getLoginErrorLimit() + userInfo.getLoginName();
                String times = jedis.get(key);
                if(times==null){
                    jedis.setex(key,60*30,"1");
                }else{
                    jedis.incr(key);
                }
                return null;
            }
        });
    }
}