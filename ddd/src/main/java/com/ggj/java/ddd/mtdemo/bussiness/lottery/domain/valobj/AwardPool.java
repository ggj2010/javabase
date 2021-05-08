package com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author gaoguangjin
 */
public class AwardPool {
    private String cityIds;//奖池支持的城市
    private String scores;//奖池支持的得分
    private int userGroupType;//奖池匹配的用户类型
    private List<Award> awards;//奖池中包含的奖品

    //当前奖池是否与城市匹配
    public boolean matchedCity(Integer cityId) {
        if(cityIds.equals(String.valueOf(cityId))){
            return true;
        }

        return false;
    }

    //当前奖池是否与用户得分匹配
    public boolean matchedScore(int score) {
        if(cityIds.equals(String.valueOf(scores))){
            return true;
        }
        return false;
    }

    //根据概率选择奖池
    public Award randomGetAward() {
        int sumOfProbablity = 0;
        for (Award award : awards) {
            sumOfProbablity += award.getAwardProbablity();
        }
        int randomNumber = ThreadLocalRandom.current().nextInt(sumOfProbablity);
        int range = 0;
        for (Award award : awards) {
            range += award.getProbablity();
            if (randomNumber < range) {
                return award;
            }
        }
        return null;
    }


    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public int getUserGroupType() {
        return userGroupType;
    }

    public void setUserGroupType(int userGroupType) {
        this.userGroupType = userGroupType;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }
}
