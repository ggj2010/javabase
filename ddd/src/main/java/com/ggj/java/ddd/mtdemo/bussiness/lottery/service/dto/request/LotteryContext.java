package com.ggj.java.ddd.mtdemo.bussiness.lottery.service.dto.request;

/**
 * @author gaoguangjin
 */
public class LotteryContext {
    private int lotteryId; // 抽奖Id
    private Integer mtCityId;
    private int gameScore;

    public int getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Integer getMtCityId() {
        return mtCityId;
    }

    public void setMtCityId(Integer mtCityId) {
        this.mtCityId = mtCityId;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }
}
