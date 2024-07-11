package com.priska.domain.strategy.repository;

import com.priska.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 策略服务仓储接口
 * */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardRateSearchTable(Long strategyId, BigDecimal rateRange, Map<Integer, Integer> shuffleStrategyAwardRateSearchTable);

    int getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey);
}
