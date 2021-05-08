package com.ggj.java.ddd.mtdemo.bussiness.lottery.facade;

import com.ggj.java.ddd.mtdemo.bussiness.lottery.domain.valobj.DrawLotteryContext;
import com.ggj.java.ddd.mtdemo.bussiness.lottery.service.dto.request.LotteryContext;

/**
 * 用户城市信息防腐层(UserCityInfoFacade)，用于外部的用户城市信息上下文（微服务架构下表现为用户城市信息服务
 * 亦称适配层。在一个上下文中，有时需要对外部上下文进行访问，通常会引入防腐层的概念来对外部上下文的访问进行一次转义。
 *
 * 有以下几种情况会考虑引入防腐层：
 *
 * 需要将外部上下文中的模型翻译成本上下文理解的模型。
 * 不同上下文之间的团队协作关系，如果是供奉者关系，建议引入防腐层，避免外部上下文变化对本上下文的侵蚀。
 * 该访问本上下文使用广泛，为了避免改动影响范围过大。
 * 如果内部多个上下文对外部上下文需要访问，那么可以考虑将其放到通用上下文中。
 *
 * 在抽奖平台中，我们定义了用户城市信息防腐层(UserCityInfoFacade)，用于外部的用户城市信息上下文（微服务架构下表现为用户城市信息服务）。
 *
 * 以用户信息防腐层举例，它以抽奖请求参数(LotteryContext)为入参，以城市信息(MtCityInfo)为输出
 * @author gaoguangjin
 */
public class UserCityInfoFacade {

  /*  @Autowired
    private LbsService lbsService;//外部用户城市信息RPC服务*/

    public MtCityInfo getMtCityInfo(LotteryContext context) {
//        LbsReq lbsReq = new LbsReq();
//        lbsReq.setLat(context.getLat());
//        lbsReq.setLng(context.getLng());
//        LbsResponse resp = lbsService.getLbsCityInfo(lbsReq);
        return null;
    }
}
