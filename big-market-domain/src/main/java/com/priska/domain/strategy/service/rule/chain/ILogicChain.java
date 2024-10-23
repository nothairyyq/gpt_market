package com.priska.domain.strategy.service.rule.chain;

import com.priska.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖策略规则责任链接口
 * @param: 用户ID 策略ID
 * @return: 奖品ID
 * @author: Priska
 * @create: 2024-10-16
 */
public interface ILogicChain extends ILogicChainArmory{
    //抽奖策略的处理逻辑，返回 奖品对象awardVO(awardId, LogicModel)
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
}
