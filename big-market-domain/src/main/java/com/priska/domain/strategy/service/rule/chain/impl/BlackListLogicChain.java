package com.priska.domain.strategy.service.rule.chain.impl;

import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.priska.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description: 黑名单规则过滤
 * @author: Priska
 * @create: 2024-10-16
 */
@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单过滤规则开始 userId: {} strategyId: {} ruleModel: {}",userId,strategyId,ruleModel());

        //查询黑名单对应的ruleValue
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        //将用户名分组
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for(String userBlackId : userBlackIds){
            if(userId.equals(userBlackId)){
                log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return awardId;
            }
        }

        //如果没有匹配到黑名单
        //规则放行，过滤其他责任链节点
        log.info("抽奖责任链-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        return next().logic(userId,strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
