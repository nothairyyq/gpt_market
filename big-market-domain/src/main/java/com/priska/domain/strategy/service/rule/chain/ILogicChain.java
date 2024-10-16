package com.priska.domain.strategy.service.rule.chain;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖策略规则责任链接口
 * @param: 用户ID 策略ID
 * @return: 奖品ID
 * @author: Priska
 * @create: 2024-10-16
 */
public interface ILogicChain extends ILogicChainArmory{
    //抽奖策略的处理逻辑，返回奖品ID（抽奖结果）
    Integer logic(String userId, Long strategyId);
}
