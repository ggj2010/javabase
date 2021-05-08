package com.ggj.java.ddd.mtdemo.bussiness.lottery.repo;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.aggregate.DrawLottery;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.Award;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.AwardPool;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.cache.DrawLotteryCacheAccessObj;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao.AwardDao;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao.AwardPoolDao;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao.po.AwardPO;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.dao.po.AwardPoolPO;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源库对外的整体访问由Repository提供，它聚合了各个资源库的数据信息，同时也承担了资源存储的逻辑（例如缓存更新机制等）。
 * 在抽奖资源库中，我们屏蔽了对底层奖池和奖品的直接访问，而是仅对抽奖的聚合根进行资源管理。
 * 代码示例中展示了抽奖资源获取的方法（最常见的Cache Aside Pattern）。
 * 比起以往将资源管理放在服务中的做法，由资源库对资源进行管理，职责更加明确，代码的可读性和可维护性也更强。
 * @author gaoguangjin
 */
public class DrawLotteryRepository {
    private AwardDao awardDao;
    private AwardPoolDao awardPoolDao;
    private DrawLotteryCacheAccessObj drawLotteryCacheAccessObj;

    public DrawLottery getDrawLotteryById(int lotteryId) {
        System.out.println("抽奖领域服务调用仓储查询到聚合根");
        DrawLottery drawLottery = drawLotteryCacheAccessObj.get(lotteryId);
        if (drawLottery != null) {
            return drawLottery;
        }
        drawLottery = getDrawLotteyFromDB(lotteryId);
        drawLotteryCacheAccessObj.add(lotteryId, drawLottery);
        return drawLottery;
    }

    private DrawLottery getDrawLotteyFromDB(int lotteryId) {
        DrawLottery drawLottery = new DrawLottery();
        drawLottery.setLotteryId(lotteryId);

        List<AwardPool> awardPools=new ArrayList<>();
        List<AwardPoolPO> awardPoolPOList=awardPoolDao.find(lotteryId);
        for (AwardPoolPO awardPoolPO : awardPoolPOList) {
            AwardPool awardPool=new AwardPool();
            //bean copy
            List<AwardPO> awardPOList= awardDao.find(awardPoolPO.getId());
            List<Award> awardList=new ArrayList<>();
            awardPool.setAwards(awardList);

            awardPools.add(awardPool);
        }
        drawLottery.setAwardPools(awardPools);
        return drawLottery;
    }
}
