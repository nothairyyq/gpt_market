package com.priska.domain.strategy.service.rule.tree.factory.engine;

import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @program: IntelliJ IDEA
 * @description:规则树组合接口
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-10-16
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId);
}
