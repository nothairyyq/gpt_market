package com.priska.domain.strategy.repository;

import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.model.entity.StrategyEntity;
import com.priska.domain.strategy.model.entity.StrategyRuleEntity;
import com.priska.domain.strategy.model.valobj.RuleTreeVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardRuleModelVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 策略服务仓储接口
 * */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    //void storeStrategyAwardRateSearchTable(Long strategyId, BigDecimal rateRange, Map<Integer, Integer> shuffleStrategyAwardRateSearchTable);

    void storeStrategyAwardRateSearchTable(String key, BigDecimal rateRange, Map<Integer, Integer> shuffleStrategyAwardRateSearchTable);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey);

    Integer getStrategyAwardAssemble(String key, Integer rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);


    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    //根据treeId查询规则树结构信息，返回RuleTreeVO
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    /*
    * 缓存奖品库存
    *
    * @param cacheKey key
    * @param awardCount 库存值
    * */
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    /**
     * 缓存key，decr 方式扣减库存
     *
     * @param cacheKey 缓存Key
     * @return 扣减结果
     */
    Boolean subtractionAwardStock(String cacheKey);
}
