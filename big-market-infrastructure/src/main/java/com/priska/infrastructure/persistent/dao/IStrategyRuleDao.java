package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {
    List<StrategyRule> queryStrategyRuleList();
    StrategyRule queryStrategyRule(StrategyRule strategyRuleReq);
}
