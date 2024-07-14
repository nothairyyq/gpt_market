package com.priska.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description:规则物料实体对象，用于过滤规则的必要信息
 * @author: Priska
 * @create: 2024-07-13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleMatterEntity {
    /*用户id*/
    private String userId;
    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖奖品ID【规则类型为策略，则不需要奖品ID】*/
    private Integer awardId;
    /*抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】*/
    private String ruleModel;
}
