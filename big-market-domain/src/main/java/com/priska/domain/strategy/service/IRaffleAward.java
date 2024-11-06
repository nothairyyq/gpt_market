package com.priska.domain.strategy.service;

import com.priska.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 奖品接口 含获取奖品列表接口
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-11-06
 */
public interface IRaffleAward {

    /***
     * 根据策略ID查询抽奖奖品列表
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
