package com.ggj.java.spring.intercept;

import org.springframework.stereotype.Service;

/**
 * @author gaoguangjin
 */
@Service
public class Movie {

    @RemoteClass(className = "com.ggj.java.spring.intercept.ZhouXinChi")
    private ActorInterface actorInterface;

    public void paly(){
        actorInterface.palyStyle();
    }
}
