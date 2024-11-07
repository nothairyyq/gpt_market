package com.priska.domain.strategy.repository;

import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.model.entity.StrategyEntity;
import com.priska.domain.strategy.model.entity.StrategyRuleEntity;
import com.priska.domain.strategy.model.valobj.RuleTreeVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

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

    /**
     * 写入奖品库存消费队列
     *
     * @param strategyAwardStockKeyVO 对象值对象
     */
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    /**
     * 获取奖品库存消费队列
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗
     *
     * @param strategyId 策略ID
     * @param awardId 奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    /**
     * 根据策略id+奖品id的唯一组合查询奖品信息
     * @param strategyId 策略id
     * @param awardId    奖品id
     * @return StrategyAwardEntity 奖品实体
     */
    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);
}
