package com.priska.infrastructure.persistent.repository;

import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.infrastructure.persistent.dao.IStrategyAwardDao;
import com.priska.infrastructure.persistent.po.StrategyAward;
import com.priska.infrastructure.persistent.redis.IRedisService;
import com.priska.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* 实现策略仓储接口
* */
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //先从redis中查找strategyID的策略奖品实体
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        //如果在redis中查找的到，就返回策略奖品实体
        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty())
            return strategyAwardEntities;

        //如果在redis中查找不到，在库中读取数据
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards){
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }

        redisService.setValue(cacheKey,strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardRateSearchTable(Long strategyId, BigDecimal rateRange, Map<Integer, Integer> shuffleStrategyAwardRateSearchTable) {
        //1.存储抽奖策略范围值，如10000
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+strategyId, rateRange.intValue());
        //2.存储概率查找表
        Map<Integer,Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+strategyId);
        cacheRateTable.putAll(shuffleStrategyAwardRateSearchTable);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+strategyId);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+strategyId,rateKey);
    }
}
