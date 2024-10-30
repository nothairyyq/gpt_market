package com.priska.domain.strategy.service.rule.tree.impl;

import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.priska.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: IntelliJ IDEA
 * @description: 次数锁
 * @author: Priska
 * @create: 2024-10-16
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {


    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        return null;
    }
}
