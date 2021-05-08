package com.ggj.java.spring.intercept;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gaoguangjin
 */
@Slf4j
public class GuoDeGang implements ActorInterface {

    @Override
    public void palyStyle() {
        log.info("gdg paly");
    }
}
