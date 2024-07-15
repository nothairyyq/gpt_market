package com.priska.domain.strategy.service.rule.impl;

import com.priska.domain.strategy.model.entity.RuleActionEntity;
import com.priska.domain.strategy.model.entity.RuleMatterEntity;
import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.annotation.LogicStrategy;
import com.priska.domain.strategy.service.rule.ILogicFilter;
import com.priska.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.priska.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: IntelliJ IDEA
 * @description: [抽奖前规则]根据抽奖权重返回可抽奖范围Key
 * @author: Priska
 * @create: 2024-07-14
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
    @Resource
    private IStrategyRepository repository;
    public Long userScore = 4500L;
    /*
    * 权重规则过滤
    * 权重规则:4000:102,103,104,105 5000:102,103,104,105,106,107
    * 解析数据格式，判断哪个范围符合用户的特定抽奖范围
    *
    * @param ruleMatterEntity 规则物料实体对象
    * @return 规则行为实体（规则过滤结果）
    * */
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity){
        log.info("规则过滤--权重范围 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(),ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());
        String userId = ruleMatterEntity.getUserId();
        Long strategyId = ruleMatterEntity.getStrategyId();
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());

        //1.根据用户ID查询用户抽奖消耗的积分值，按道理来说应该从数据库中获取，但是现在先按照固定值写
        //1.1 从ruleValue中获取每一组的数据： {4000：【4000:102，103，。。。】，5000：【5000:103，104.。。】}
        Map<Long, String> splitWeightValueGroup = getSplitWeightValue(ruleValue);

        //2. 将key取出来：4000，5000，6000。。。。
        List<Long> splitSortedWeightKeys = new ArrayList<>(splitWeightValueGroup.keySet());
        Collections.sort(splitSortedWeightKeys);

        //3。找出最小符合的值
        Long nextValue = splitSortedWeightKeys.stream()
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);
        if (null != nextValue){
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .strategyId(strategyId)
                            .ruleWeightValueKey(splitWeightValueGroup.get(nextValue))
                            .build())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }

    //4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107
    private Map<Long, String> getSplitWeightValue(String ruleValue) {
        Map<Long, String> ruleWeightValueMap = new HashMap<>();

        String[] ruleWeightValueGroup = ruleValue.split(Constants.SPACE);
        for (String ruleWeightValueKey : ruleWeightValueGroup){
            if (ruleWeightValueKey == null || ruleWeightValueKey.isEmpty())
                return  ruleWeightValueMap;

            String[] parts = ruleWeightValueKey.split(Constants.COLON);
            if (parts.length != 2){
                throw new IllegalArgumentException("rule_weight rule_value invalid input format" + ruleWeightValueKey);
            }
            ruleWeightValueMap.put(Long.parseLong(parts[0]),ruleWeightValueKey);
        }

        return ruleWeightValueMap;
    }
}
