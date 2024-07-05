package com.priska.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

//帮忙生成get和set方法
@Data
public class StrategyRule {

    /*自增ID*/
    private Long id;
    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖奖品ID【规则类型为策略，则不需要奖品ID】*/
    private Integer awardId;
    /*抽象规则类型；1-策略规则、2-奖品规则*/
    private Integer ruleType;
    /*抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】*/
    private String rule_model;
    /*抽奖规则比值*/
    private String rule_value;
    /*抽奖规则描述*/
    private String rule_desc;
    /*创建时间*/
    private Date create_time;
    /*更新时间*/
    private Date update_time;

}