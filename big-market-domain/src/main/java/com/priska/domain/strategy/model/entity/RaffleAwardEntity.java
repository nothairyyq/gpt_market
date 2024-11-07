package com.priska.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖奖品实体 对应AwardPO. sql中award那张表
 * @author: Priska
 * @create: 2024-07-13
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {
//    /*策略id*/
//    private Long strategyId;
    /*抽奖奖品ID - 内部流转使用*/
    private Integer awardId;
//    /*奖品对接标识 - 每一个都是一个对应的发奖策略*/
//    private String awardKey;
    /*奖品配置信息*/
    private String awardConfig;
//    /*奖品内容描述*/
//    private String awardDesc;
    /*奖品顺序*/
    private Integer sort;
}
