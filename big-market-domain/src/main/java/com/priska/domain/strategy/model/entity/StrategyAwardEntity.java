package com.priska.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/*
* 策略奖品实体，po数据持久化对象和实体对象基本是1对1，需要必要字段
* */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardEntity {

    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖奖品ID - 内部流转使用*/
    private Integer awardId;
    /*奖品标题*/
    private String awardTitle;
    /*奖品副标题*/
    private String awardSubtitle;
    /*奖品库存总量*/
    private Integer awardCount;
    /*奖品库存剩余*/
    private Integer awardCountSurplus;
    /*奖品中奖概率*/
    private BigDecimal awardRate;
    /*奖品排序值*/
    private Integer sort;
}
