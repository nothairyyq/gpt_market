package com.priska.domain.strategy.service.rule.impl;

import com.priska.domain.strategy.model.entity.RuleActionEntity;
import com.priska.domain.strategy.model.entity.RuleMatterEntity;
import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.annotation.LogicStrategy;
import com.priska.domain.strategy.service.rule.ILogicFilter;
import com.priska.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description: 次数规则过滤，即用户抽奖n次后，解锁对应的抽奖奖品以刺激用户消耗积分
 * @author: Priska
 * @create: 2024-10-13
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {
    @Resource
    private IStrategyRepository repository;
    //用户抽奖次数，现在先自定义，后续会从数据库/redis中获取
    private Long userRaffleCount = 0L;
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-次数锁 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        //查询ruleValue. 抽奖到某样奖品时，对应awardId的rulevalue.
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        long raffleCount = Long.parseLong(ruleValue);


        //filter用户次数和raffleCount的关系
        //1. 如果用户抽奖次数userRaffleCount 大于ruleValue， filter放行用户
        if (userRaffleCount >= raffleCount){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        //2.如果用户抽奖次数小于设定的值，返回拦截ruleAction
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
