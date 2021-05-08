# 领域驱动设计在互联网业务开发中的实践

   https://tech.meituan.com/2017/12/22/ddd-in-practice.html

### 为什么要用ddd
    系统架构不清晰，划分出来的模块内聚度低、高耦合
    有三种解决方式：
    1、按照演进式设计的理论，让系统的设计随着系统实现的增长而增长。我们不需要作提前设计，就让系统伴随业务成长而演进
    2、敏捷实践中的重构
        重构是克服演进式设计中大杂烩问题的主力，通过在单独的类及方法级别上做一系列小步重构来完成
    3、测试驱动设计及持续集成
### 贫血症和失忆症
    贫血领域对象（Anemic Domain Object）是指仅用作数据载体，而没有行为和动作的领域对象
    如果系统过于复杂，业务逻辑、状态会散落到在大量方法中，原本的代码意图会渐渐不明确，我们将这种情况称为由贫血症引起的失忆症

    例如人员、机构、权限三个模块，早期系统比较简单，可能只是人员模块依赖机构模块与权限模块，后面随着系统演变，模块之间边界
    越来越模糊，可能机构模块依赖人员模块，权限模块也依赖人员模块，这样就形成了循环依赖。

    采用领域模型的开发方式，将数据和行为封装在一起，并与现实世界中的业务对象相映射。
    各类具备明确的职责划分，将领域逻辑分散到领域对象中。

## 需求
    产品的需求概述如下：
    1. 抽奖活动有活动限制，例如用户的抽奖次数限制，抽奖的开始和结束的时间等；
    2. 一个抽奖活动包含多个奖品，可以针对一个或多个用户群体；
    3. 奖品有自身的奖品配置，例如库存量，被抽中的概率等，最多被一个用户抽中的次数等等；
    4. 用户群体有多种区别方式，如按照用户所在城市区分，按照新老客区分等；
    5. 活动具有风控配置，能够限制用户参与抽奖的频率。

    对于活动的限制，我们定义了活动准入的通用语言，将活动开始/结束时间，
    活动可参与次数等限制条件都收拢到活动准入上下文中。
    对于抽奖的奖品库存量，由于库存的行为与奖品本身相对解耦，库存关注点更多是库存内容的核销，
    且库存本身具备通用性，可以被奖品之外的内容使用，因此我们定义了独立的库存上下文。
    由于C端存在一些刷单行为，我们根据产品需求定义了风控上下文，用于对活动进行风控。
    最后，活动准入、风控、抽奖等领域都涉及到一些次数的限制，因此我们定义了计数上下文
    通过DDD的限界上下文划分，我们界定出抽奖、活动准入、风控、计数、库存等五个上下文，每个上下文在系统中都高度内聚
## DDD工程实现
    模块的组织
    import com.company.team.bussiness.lottery.*;//抽奖上下文
    import com.company.team.bussiness.riskcontrol.*;//风控上下文
    import com.company.team.bussiness.counter.*;//计数上下文
    import com.company.team.bussiness.condition.*;//活动准入上下文
    import com.company.team.bussiness.stock.*;//库存上下文

    模块内的组织
    import com.company.team.bussiness.lottery.domain.valobj.*;//领域对象-值对象
    import com.company.team.bussiness.lottery.domain.entity.*;//领域对象-实体
    import com.company.team.bussiness.lottery.domain.aggregate.*;//领域对象-聚合根
    import com.company.team.bussiness.lottery.service.*;//领域服务
    import com.company.team.bussiness.lottery.repo.*;//领域资源库
    import com.company.team.bussiness.lottery.facade.*;//领域防腐层
