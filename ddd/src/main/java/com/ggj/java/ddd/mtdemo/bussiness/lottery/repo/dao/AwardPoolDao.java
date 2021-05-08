package com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao.po.AwardPoolPO;

import java.util.List;

public interface AwardPoolDao {
    List<AwardPoolPO> find(int lotteryId);
}
