package com.ggj.java.ddd.mtdemo;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.service.LotteryService;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.service.dto.request.LotteryContext;

/**
 * 应用服务层，聚合多个领域层业务
 *
 * @author gaoguangjin
 */
public class LotteryApplicationService {
    /*  private LotteryRiskService riskService;
      private LotteryConditionService conditionService;*/
    private LotteryService lotteryService;

    //用户参与抽奖活动
    public String participateLottery(LotteryContext lotteryContext) {
        //校验用户登录信息
        validateLoginInfo(lotteryContext);
        //校验风控
        //RiskAccessToken riskToken = riskService.accquire(buildRiskReq(lotteryContext));
        //活动准入检查
        //LotteryConditionResult conditionResult = conditionService.checkLotteryCondition(otteryContext.getLotteryId(),lotteryContext.getUserId());
        //抽奖并返回结果
        String issueResponse = lotteryService.issueLottery(lotteryContext);
        if (issueResponse != null) {
            return null;
        } else {
            return null;
        }
    }

    private void validateLoginInfo(LotteryContext lotteryContext) {
        System.out.println("valid ");
        return;
    }
}
