package com.priska.domain.strategy.service.rule;

import com.priska.domain.strategy.model.entity.RuleActionEntity;
import com.priska.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖规则过滤接口
 * @param: 规则物资，基本信息
 * @return: 规则行为实体
 * @author: Priska
 * @create: 2024-07-13
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
