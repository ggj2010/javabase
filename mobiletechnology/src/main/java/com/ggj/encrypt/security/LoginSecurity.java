package com.ggj.encrypt.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggj.encrypt.common.constant.GlobalConstant;
import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.persistence.ApiUserToken;
import com.ggj.encrypt.common.persistence.HttpHeaderParam;
import com.ggj.encrypt.common.utils.ApiTools;
import com.ggj.encrypt.common.utils.MD5Util;
import com.ggj.encrypt.common.utils.TokenHelper;
import com.ggj.encrypt.common.utils.redis.RedisDaoTemplate;
import com.ggj.encrypt.common.utils.redis.callback.RedisCallback;
import com.ggj.encrypt.configuration.RedisKeyConfiguration;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.configuration.SystemConfiguration;
import com.ggj.encrypt.modules.sys.bean.UserInfo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/26 18:41
 */
@Service
@Slf4j
public class LoginSecurity {
    private static final String DEFAULT_CHARSET = "UTF-8";
    @Autowired
    private RedisDaoTemplate redisDaoTemplate;
    @Autowired
    private SystemConfiguration systemConfiguration;
    @Autowired
    private RedisKeyConfiguration redisKeyConfiguration;
    @Autowired
    private ResultCodeConfiguration resultCodeConfiguration;
    @Autowired
    private TokenHelper tokenHelper;

    /**
     * 接口调用频繁 直接不返回结果
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public Boolean checkIPAndInvokeNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 检查ip是否黑名单
        String ip = ApiTools.getIpAddr(request);
        return redisDaoTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(Jedis jedis) throws BizException {
                Map<String, String> ipMap = new HashMap<String, String>();
                String blackipKey = redisKeyConfiguration.getBlackip();
                String ipIvokeTimekey = redisKeyConfiguration.getIpInvoke() + ip;
                // 是否启用黑名单功能
                if (systemConfiguration.getIsBlackipLimit().equals(GlobalConstant.TREU)) {
                    //ip是否在黑名单里面
                    if (!jedis.hexists(blackipKey, ip)) {
                        //ip1分钟调用次数
                        if (jedis.exists(ipIvokeTimekey)) {
                            if (jedis.incr(ipIvokeTimekey) > Long.parseLong(systemConfiguration.getIpMaxInvokeNumber())) {
                                //ip一分钟调用太多次直接拉入黑名单
                                ipMap.put(ip, "1");
                                jedis.hmset(blackipKey, ipMap);
                                log.error("ip访问频繁:=" + ip);
                                //throw new BizException(resultCodeConfiguration.getIpMaxInvoke(), resultCodeConfiguration.getIpMaxInvokeMsg());
                                return false;
                            }
                        } else {
                            jedis.incr(ipIvokeTimekey);
                        }
                        jedis.expire(ipIvokeTimekey, 60);
                    } else {
                        //黑名单调用次数
                        String count=jedis.hmget(blackipKey,ip).get(0);
                        log.error("黑名单ip="+ip+";访问url="+request.getRequestURI()+";掉用接口次数"+count);
                        ipMap.put(ip,(Integer.parseInt(count)+1)+"");
                        jedis.hmset(blackipKey, ipMap);
                        return false;
                        //throw new BizException(resultCodeConfiguration.getBlackipCode(), resultCodeConfiguration.getBlackipMsg());
                    }
                }
                return true;
            }
        }
        );
    }

    /**
     * 检验用户是否登陆分两步，
     * 第一，根据传过来的用户id 去查询用户的token，
     * 如果不存在表示用户没有登陆，
     * 存在的话（存在不一定代表是你本人登陆，举个例子A id=1 登陆了。这时候如果B知道你的id，就可以模拟A登陆，可以直接操作A的消息）就继续校验签名。
     * 签名里面是MD5（token+时间+接口参数）
     * 这里需要定义一个规则，校验时间戳
     *
     * @param request
     * @throws Exception
     */
    public void checkIsLoginAndValidSgnature(HttpServletRequest request) throws Exception {
        HttpHeaderParam httpHeaderParam = HttpHeaderParam.parseRequestHeader(request);
        ApiUserToken apiUserToken = tokenHelper.getUserToken(httpHeaderParam.getUserId());
        validSignature(httpHeaderParam, apiUserToken.getToken());
    }

    /**
     * 接口签名参数校验
     * APPKEY+TIMESTAMP+USERTOKEN
     * 也可以检验时间的大小
     *
     * @param httpHeaderParam
     * @param token
     * @throws Exception
     */
    private void validSignature(HttpHeaderParam httpHeaderParam, String token) throws Exception {
        String sing = httpHeaderParam.getAppkey() + httpHeaderParam.getTimeStamp() + token;
        if (!MD5Util.md5Encode(httpHeaderParam.getAppkey() + httpHeaderParam.getTimeStamp() + token).equals(httpHeaderParam.getSign()))
            throw new BizException(resultCodeConfiguration.getSignatureErrorCode(), resultCodeConfiguration.getSignatureErrorCodeMsg());
    }


    /**
     * 检查登陆错误次数
     *
     * @param userInfo
     * @throws Exception
     */
    public void checkLoginErrorTimes(UserInfo userInfo) throws Exception {
        redisDaoTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(Jedis jedis) throws Exception {
                String key = redisKeyConfiguration.getLoginErrorLimit() + userInfo.getLoginName();
                String times = jedis.get(key);
                if (times != null) {
                    if (Integer.parseInt(times) >= Integer.parseInt(systemConfiguration.getLoginErrorMaxNumber())) {
                        throw new BizException(resultCodeConfiguration.getLoginErrorLimit(), resultCodeConfiguration.getLoginErrorLimitMsg());
                    }
                }
                return null;
            }
        });
    }

    /**
     * 添加登陆错误次数
     *
     * @param userInfo
     * @throws Exception
     */
    public void addLoginErrorTimes(UserInfo userInfo) throws Exception {
        redisDaoTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(Jedis jedis) throws Exception {
                String key = redisKeyConfiguration.getLoginErrorLimit() + userInfo.getLoginName();
                String times = jedis.get(key);
                if (times == null) {
                    jedis.setex(key, 60 * 30, "1");
                } else {
                    jedis.incr(key);
                }
                return null;
            }
        });
    }
}