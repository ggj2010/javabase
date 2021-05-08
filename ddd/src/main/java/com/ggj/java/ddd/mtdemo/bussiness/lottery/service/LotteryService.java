package com.ggj.java.ddd.mtdemo.bussiness.lottery.service;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.DrawLotteryContext;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.service.dto.request.LotteryContext;

/**
 * @author gaoguangjin
 */
public interface LotteryService {
    public String issueLottery(LotteryContext lotteryContext);
}
