package com.priska.domain.strategy.service.rule.tree;

import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树接口
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-10-16
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue);
}
