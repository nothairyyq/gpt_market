package com.priska.domain.strategy.service.rule.tree.impl;

import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.armory.IStrategyDispatch;
import com.priska.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description: 库存扣减节点
 * @author: Priska
 * @create: 2024-10-16
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch strategyDispatch;
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则树过滤-库存扣减 userID:{} strategyID:{} awardID:{} ruleValue:{}", userId, strategyId, awardId, ruleValue);

        Boolean status =  strategyDispatch.subtractionAwardStock(strategyId,awardId);
        if (status){
            log.info("规则过滤-库存扣减成功");

            //将扣减库存写入延迟队列，延迟消费更新数据库的记录
            //使用trigger层的job: UpdateAwardStockJob 消费延迟队列，更新数据库记录
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                            .strategyId(strategyId)
                            .awardId(awardId)
                            .build());

            //库存扣减成功就被接管了
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue(ruleValue)
                            .build())
                    .build();
        }


        // 如果库存不足或者扣减库存失败（加锁失败），则直接返回放行
        log.warn("规则过滤-库存扣减-告警，库存不足。userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
