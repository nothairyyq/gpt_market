package com.priska.domain.strategy.service.raffle;


import com.priska.domain.strategy.model.valobj.RuleTreeVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.AbstractRaffleStrategy;
import com.priska.domain.strategy.service.armory.IStrategyDispatch;
import com.priska.domain.strategy.service.rule.chain.ILogicChain;
import com.priska.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.priska.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖策略实现
 * @author: Priska
 * @create: 2024-07-15
 */
@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {



    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId,strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        //如果没有任何奖品对应的rule model，直接返回奖品
        if (strategyAwardRuleModelVO == null){
            return DefaultTreeFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }

        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (ruleTreeVO == null){
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 "+strategyAwardRuleModelVO.getRuleModels());
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }


    ;
}
