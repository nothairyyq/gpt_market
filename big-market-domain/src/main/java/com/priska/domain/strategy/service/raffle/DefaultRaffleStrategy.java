package com.priska.domain.strategy.service.raffle;


import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.model.valobj.RuleTreeVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.AbstractRaffleStrategy;
import com.priska.domain.strategy.service.IRaffleAward;
import com.priska.domain.strategy.service.IRaffleStock;
import com.priska.domain.strategy.service.armory.IStrategyDispatch;
import com.priska.domain.strategy.service.rule.chain.ILogicChain;
import com.priska.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.priska.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖策略实现
 * @author: Priska
 * @create: 2024-07-15
 */
@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward {



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

    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
     */
    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    /***
     * 根据策略ID查询抽奖奖品列表
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }


    ;
}
