package com.priska.domain.strategy.service.armory;


import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.repository.IStrategyRepository;
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
public class StrategyArmory implements  IStrategyArmory{

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        if (strategyAwardEntities == null || strategyAwardEntities.isEmpty()) return  false;
        //2. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //3. 获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //4. 概率值总和除以最小概率值获得每一份的奖品
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        //5. 生成策略奖品概率查找表. 在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高
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

        //6.对奖品存储表乱序
        Collections.shuffle(strategyAwardRateSearchTable);

        //7.生成map集合。 key对应的是后续的概率值。value是awardId. 通过概率获得对应的奖品id
        Map<Integer, Integer> shuffleStrategyAwardRateSearchTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardRateSearchTable.size(); i++){
            shuffleStrategyAwardRateSearchTable.put(i, strategyAwardRateSearchTable.get(i));
        }

        //8. 存放到redis中
        repository.storeStrategyAwardRateSearchTable(strategyId, rateRange, shuffleStrategyAwardRateSearchTable);
        return true;
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        //分布式部署，不一定为当前应用做的策略装配，值不一定会保存到本应用。是分布式应用，所以需要从Redis中获取
        int rateRange = repository.getRateRange(strategyId);
        //通过生成的随机值，获取概率值奖品查找表的结果
        return  repository.getStrategyAwardAssemble(strategyId,new SecureRandom().nextInt(rateRange));
    }
}
