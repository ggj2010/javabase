package com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj;

/**
 * @author gaoguangjin
 */
public class DrawLotteryContext {
    private int lotteryId; // 抽奖Id
    private Integer mtCityId;
    private int gameScore;

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }


    public Integer getMtCityId() {
        return mtCityId;
    }

    public void setMtCityId(Integer mtCityId) {
        this.mtCityId = mtCityId;
    }

    public int getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
    }
}
