package com.priska.domain.strategy.service.rule.chain.impl;

import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.armory.IStrategyDispatch;
import com.priska.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.priska.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.priska.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: IntelliJ IDEA
 * @description: 规则权重过滤
 * @author: Priska
 * @create: 2024-10-16
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Resource
    private IStrategyDispatch strategyDispatch;

    //根据用户Id在仓储中查询用户抽奖消耗的积分值。后续做这里的处理。
    private Long userScore = 0L;

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重过滤开始 userID:{} strategyID:{} ruleModel:{}",userId,strategyId,ruleModel());

        //从仓库中查询rule_value
        //4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
        String ruleValue = repository.queryStrategyRuleValue(strategyId,ruleModel());

        //1.根据用户ID查询用户抽奖消耗的积分值，按道理来说应该从数据库中获取，但是现在先按照固定值写
        //1.1 从ruleValue中获取每一组的数据： {4000：【4000:102，103，。。。】，5000：【5000:103，104.。。】}
        Map<Long, String> splitWeightValueGroup = getSplitWeightValue(ruleValue);
        if(splitWeightValueGroup == null || splitWeightValueGroup.isEmpty()) return null;

        //2. 将key取出来：4000，5000，6000。。。。默认排序
        List<Long> splitSortedWeightKeys = new ArrayList<>(splitWeightValueGroup.keySet());
        Collections.sort(splitSortedWeightKeys);

        //3。找出最小符合的值
        //也就是【4500 积分，能找到 4000:102,103,104,105】、【5000 积分，能找到 5000:102,103,104,105,106,107】
        Long nextValue = splitSortedWeightKeys.stream()
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);


        //4. 权重抽奖，如果有符合的nextValue,返回抽奖结果
        if(nextValue != null){
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId,splitWeightValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
            return DefaultChainFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .logicModel(ruleModel())
                    .build();
        }

        //5.传递给下一个责任链节点，过滤其他规则
        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId,strategyId);
    }

    //4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107
    //return: {4000:{4000:102,103,104,105}, 5000:{5000:102,103,104,105,106,107}}
    private Map<Long, String> getSplitWeightValue(String ruleValue) {
        Map<Long, String> ruleWeightValueMap = new HashMap<>();

        //[4000:102,103,104,105 . 5000:102,103,104,105,106,107 ]
        String[] ruleWeightValueGroup = ruleValue.split(Constants.SPACE);
        for (String ruleWeightValueKey : ruleWeightValueGroup){
            if (ruleWeightValueKey == null || ruleWeightValueKey.isEmpty())
                return  ruleWeightValueMap;

            //[4000, 102,103,104,105]
            String[] parts = ruleWeightValueKey.split(Constants.COLON);
            if (parts.length != 2){
                throw new IllegalArgumentException("rule_weight rule_value invalid input format" + ruleWeightValueKey);
            }
            //{4000:{4000:102,103,104,105}}
            ruleWeightValueMap.put(Long.parseLong(parts[0]),ruleWeightValueKey);
        }

        return ruleWeightValueMap;
    }
}
