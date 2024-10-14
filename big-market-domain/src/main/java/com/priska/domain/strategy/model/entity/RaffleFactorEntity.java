package com.priska.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 抽奖因子实体
* */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaffleFactorEntity {
    //用户id
    private String userId;
    //策略id
    private Long strategyId;
    private Integer awardId;
}
