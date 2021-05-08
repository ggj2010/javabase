package com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao.po;

/**
 * @author xch
 * @version 1.00
 * @date 2018-01-05
 */
public class AwardPoolPO {

    /**
     * 奖池支持的城市
     */
    private String cityIds;
    /**
     *奖池支持的得分
     */
    private String scores;
    /**
     * 奖池匹配的用户类型
     */
    private int userGroupType;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
