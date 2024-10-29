package com.priska.domain.strategy.service.armory;

/*
 * 策略调度接口，抽奖时调用
 * */
public interface IStrategyDispatch {
    //获取抽奖策略装配的随即结果

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId);


    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);
}
