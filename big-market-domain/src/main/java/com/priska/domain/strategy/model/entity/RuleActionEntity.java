package com.priska.domain.strategy.model.entity;

import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.*;

/**
 * @program: IntelliJ IDEA
 * @description: 规则动作实体
 * @author: Priska
 * @create: 2024-07-13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {
    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity{

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    //抽奖前规则动作实体
    static public class RaffleBeforeEntity extends RaffleEntity{
        /*策略Id*/
        private Long strategyId;
        /*抽奖时使用权重值抽奖*/
        private String ruleWeightValueKey;
        /*奖品Id*/
        private Integer awardId;
    }

    static public class RaffleCenterEntity extends RaffleEntity{}

    static public class RaffleAfterEntity extends RaffleEntity{}
}
