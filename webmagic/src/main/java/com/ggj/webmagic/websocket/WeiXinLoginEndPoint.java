package com.ggj.webmagic.websocket;

/**
 * @author:gaoguangjin
 * @date 2016/9/21 11:23
 */

import com.ggj.webmagic.base.Result;
import com.ggj.webmagic.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.TimeUnit;


/**
 * http://wiselyman.iteye.com/blog/2003336
 */
@Slf4j
@Service
@ServerEndpoint(value = "/weixin/login/state")
public class WeiXinLoginEndPoint {
    private  RedisTemplate<String, String> redisTemplate;


    @OnMessage
    public void handleMessage(Session session, String message) {
        this.redisTemplate= SpringContextHolder.getBean("stringRedisTemplate");
        log.info("input param message="+message);
        //定义token 2分钟失效 失效退出循环
        redisTemplate.opsForValue().set(message,LoginStatus.invalid.toString(),2, TimeUnit.MINUTES);
        try {
            while(true){
                String code = redisTemplate.opsForValue().get(message);
                if(StringUtils.isNotEmpty(code)) {
                    if (LoginStatus.login.toString().equals(code)){
                        session.getBasicRemote().sendText(new Result("0000","登录成功!",message).toJSONString());
                        break;
                    }
                }else{
                    session.getBasicRemote().sendText(new Result("4444","网页token失效!").toJSONString());
                    break;
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnOpen
    public void onOpen(Session session) {
        log.info("Client connected ");
    }

    @OnClose
    public void onClose() {
        log.info("Connection closed");
    }

     enum LoginStatus  {
        invalid(0),login(1);
         Integer status;
         LoginStatus(Integer status){
            this.status=status;
        }
         public Integer getStatus() {
             return status;
         }
         @Override
         public String toString() {
             return this.status.toString();
         }
     }
}
