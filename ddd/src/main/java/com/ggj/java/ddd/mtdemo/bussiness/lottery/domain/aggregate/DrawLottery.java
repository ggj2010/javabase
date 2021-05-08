package com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.aggregate;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.AwardPool;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.DrawLotteryContext;

import java.util.List;

/**
 * 聚合由根实体，值对象和实体组成。
 *
 * 如何创建好的聚合？
 *
 * 边界内的内容具有一致性：在一个事务中只修改一个聚合实例。如果你发现边界内很难接受强一致，不管是出于性能或产品需求的考虑，应该考虑剥离出独立的聚合，采用最终一致的方式。
 * 设计小聚合：大部分的聚合都可以只包含根实体，而无需包含其他实体。即使一定要包含，可以考虑将其创建为值对象。
 * 通过唯一标识来引用其他聚合或实体：当存在对象之间的关联时，建议引用其唯一标识而非引用其整体对象。如果是外部上下文中的实体，引用其唯一标识或将需要的属性构造值对象。 如果聚合创建复杂，推荐使用工厂方法来屏蔽内部复杂的创建逻辑。
 *
 * 领域驱动要解决的一个重要的问题，就是解决对象的贫血问题
 *
 * @author gaoguangjin
 */
public class DrawLottery {
    private int lotteryId; //抽奖id
    private List<AwardPool> awardPools; //奖池列表


    public void setLotteryId(int lotteryId) {
        if (lotteryId <= 0) {
            throw new IllegalArgumentException("非法的抽奖id");
        }
        this.lotteryId = lotteryId;
    }

    //根据抽奖入参context选择奖池
    public AwardPool chooseAwardPool(DrawLotteryContext context) {
        if (context.getMtCityId() != null) {
            return chooseAwardPoolByCityInfo(awardPools, context.getMtCityId());
        } else {
            return chooseAwardPoolByScore(awardPools, context.getGameScore());
        }
    }

    //根据抽奖所在城市选择奖池
    private AwardPool chooseAwardPoolByCityInfo(List<AwardPool> awardPools, Integer mtCityId) {
        for (AwardPool awardPool : awardPools) {
            if (awardPool.matchedCity(mtCityId)) {
                return awardPool;
            }
        }
        return null;
    }

    //根据抽奖活动得分选择奖池
    private AwardPool chooseAwardPoolByScore(List<AwardPool> awardPools, int gameScore) {
        return null;
    }

    public int getLotteryId() {
        return lotteryId;
    }

    public List<AwardPool> getAwardPools() {
        return awardPools;
    }

    public void setAwardPools(List<AwardPool> awardPools) {
        this.awardPools = awardPools;
    }
}
