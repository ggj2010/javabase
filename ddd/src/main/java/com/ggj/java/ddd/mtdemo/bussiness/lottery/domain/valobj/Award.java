package com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj;

/**
 * @author gaoguangjin
 */

public class Award {
    private  int awardId;
    private  int probablity;
    private int awardProbablity;

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getProbablity() {
        return probablity;
    }

    public void setProbablity(int probablity) {
        this.probablity = probablity;
    }

    public int getAwardProbablity() {
        return awardProbablity;
    }

    public void setAwardProbablity(int awardProbablity) {
        this.awardProbablity = awardProbablity;
    }
}
