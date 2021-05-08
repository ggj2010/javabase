package com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.cache;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.aggregate.DrawLottery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式缓存访问对象-抽奖缓存访问
 *
 * @author xch
 * @version 1.00
 * @date 2018-01-05
 */
public class DrawLotteryCacheAccessObj {

    Map<Integer, DrawLottery> lotteryCache = new ConcurrentHashMap<Integer,DrawLottery>();

    public DrawLottery get(int lotteryId) {
        return lotteryCache.get(lotteryId);
    }

    public void add(int lotteryId, DrawLottery drawLottery) {
        lotteryCache.put(lotteryId,drawLottery);
    }
}
