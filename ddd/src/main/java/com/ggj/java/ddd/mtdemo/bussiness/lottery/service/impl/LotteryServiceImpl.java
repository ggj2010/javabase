package com.ggj.java.ddd.mtdemo.bussiness.lottery.service.impl;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.aggregate.DrawLottery;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.Award;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.AwardPool;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.DrawLotteryContext;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.facade.MtCityInfo;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.facade.UserCityInfoFacade;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.repo.DrawLotteryRepository;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.service.LotteryService;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.service.dto.request.LotteryContext;

/**
 * 我们将领域行为封装到领域对象中，将资源管理行为封装到资源库中，将外部上下文的交互行为封装到防腐层中。
 * 此时，我们再回过头来看领域服务时，能够发现领域服务本身所承载的职责也就更加清晰了，
 * 即就是通过串联领域对象、资源库和防腐层等一系列领域内的对象的行为，对其他上下文提供交互的接
 * @author gaoguangjin
 */
public class LotteryServiceImpl implements LotteryService {

    private DrawLotteryRepository drawLotteryRepo;
    private UserCityInfoFacade UserCityInfoFacade;

/*    private AwardSendService awardSendService;
    private AwardCounterFacade awardCounterFacade;*/


    @Override
    public String issueLottery(LotteryContext lotteryContext) {
        /**
         * 获取抽奖配置聚合根
         */
        DrawLottery drawLottery = drawLotteryRepo.getDrawLotteryById(lotteryContext.getLotteryId());
        //awardCounterFacade.incrTryCount(lotteryContext);//增加抽奖计数信息
        AwardPool awardPool = drawLottery.chooseAwardPool(bulidDrawLotteryContext(drawLottery, lotteryContext));//选中奖池
        Award award = awardPool.randomGetAward();//选中奖品
        /**
         * 发出奖品实体
         */
        //buildIssueResponse(awardSendService.sendAward(award, lotteryContext));
        return null;
    }

    private DrawLotteryContext bulidDrawLotteryContext(DrawLottery drawLottery, LotteryContext lotteryContext) {
        MtCityInfo mtCityInfo = UserCityInfoFacade.getMtCityInfo(lotteryContext);
        return new DrawLotteryContext();
    }

    /**
     * 传统三层架构写法带来的问题
     * 随着业务逻辑复杂了，业务逻辑、状态会散落到在大量方法中，原本的代码意图会渐渐不明确，我们将这种情况称为由贫血症引起的失忆症
     * @param lotteryContext
     * @return
     */
    private String normalCode(LotteryContext lotteryContext) {
        //1、dao层查出想要的贫血数据DO
        //1.1 根据lotteryId查询出来 奖池与奖项。
        //2、根据逻辑选奖池
        //3、抽奖
        //4、发送奖品
        return null;
    }

}
