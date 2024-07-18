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
 * @description: 抽奖中规则，抽中N次后可解锁该奖品
 * @author: Priska
 * @create: 2024-07-15
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    //用户抽奖次数，以后从数据库中获取
    private Long userRaffleCount = 0L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-次数锁 userID:{} strategyID:{} ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        //根据策略id，奖品id，rule_lock，查找参数。现在数据库对应的rulevalue有1，2，6
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        long raffleCount = Long.parseLong(ruleValue);

        //如果用户抽奖次数大于规则限定值，规则放行
        if (userRaffleCount >= raffleCount){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        //如果用户抽奖次数小于规则限定值，规则拦截
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
