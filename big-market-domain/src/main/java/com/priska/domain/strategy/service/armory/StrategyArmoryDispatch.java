package com.priska.domain.strategy.service.armory;


import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.model.entity.StrategyEntity;
import com.priska.domain.strategy.model.entity.StrategyRuleEntity;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.types.enums.ResponseCode;
import com.priska.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/*
* 实现策略装配库的类 策略装配库，负责初始化策略计算
* */

@Slf4j
//实现类要加Service注解
@Service
public class StrategyArmoryDispatch implements  IStrategyArmory, IStrategyDispatch{

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        //2. 权重策略配置-适用于rule_model字段含有rule_weight的权重规则配置
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) return false;
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId,ruleWeight);

        //3.如果从strategy中得到的rulemodel有ruleweight但是strategyRule中查询不到ruleweight的数据，应该抛出异常
        if (null == strategyRuleEntity){
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys){
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            //移除策略奖品中没有包含在规则权重中的奖品
            strategyAwardEntitiesClone.removeIf(strategyAwardEntity -> !ruleWeightValues.contains(strategyAwardEntity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        return true;
    }

    private boolean assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities){
        //1. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //2. 获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //3. 概率值总和除以最小概率值获得每一份的奖品
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        //4. 生成策略奖品概率查找表. 在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高
        List<Integer> strategyAwardRateSearchTable = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : strategyAwardEntities){
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();
            //计算出每个概率值需要存放到查找表的数量，循环填充
            //拿总数量成每一个奖品的概率，把这些奖品全部填充进查询表里
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++){
                strategyAwardRateSearchTable.add(awardId);
            }
        }

        //5.对奖品存储表乱序
        Collections.shuffle(strategyAwardRateSearchTable);

        //6.生成map集合。 key对应的是后续的概率值。value是awardId. 通过概率获得对应的奖品id
        Map<Integer, Integer> shuffleStrategyAwardRateSearchTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardRateSearchTable.size(); i++){
            shuffleStrategyAwardRateSearchTable.put(i, strategyAwardRateSearchTable.get(i));
        }

        //7. 存放到redis中
        repository.storeStrategyAwardRateSearchTable(key, new BigDecimal(shuffleStrategyAwardRateSearchTable.size()), shuffleStrategyAwardRateSearchTable);
        return true;
    }
    @Override
    public Integer getRandomAwardId(Long strategyId) {
        //分布式部署，不一定为当前应用做的策略装配，值不一定会保存到本应用。是分布式应用，所以需要从Redis中获取
        int rateRange = repository.getRateRange(strategyId);
        //通过生成的随机值，获取概率值奖品查找表的结果
        return  repository.getStrategyAwardAssemble(strategyId,new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        int rateRange = repository.getRateRange(key);
        //通过生成的随机值，获取概率值奖品查找表的结果
        return  repository.getStrategyAwardAssemble(key,new SecureRandom().nextInt(rateRange));
    }
}
