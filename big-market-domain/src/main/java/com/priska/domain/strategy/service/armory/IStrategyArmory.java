package com.priska.domain.strategy.service.armory;

/*
* 策略装配库，负责初始化策略计算
* */
public interface IStrategyArmory {

    //装配抽奖策略配置，触发的时机可以为活动审核通过后调用
    boolean assembleLotteryStrategy(Long strategyId);

    //获取抽奖策略装配的随即结果
    Integer getRandomAwardId(Long strategyId);
}
