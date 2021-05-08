package com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao.po.AwardPO;

import java.util.List;

public interface AwardDao {
    void get();

    List<AwardPO> find(int id);
}
