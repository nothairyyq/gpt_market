package com.priska.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* 规则过滤校验类型值对象
* */
@AllArgsConstructor
@Getter
public enum RuleLogicCheckTypeVO {

    ALLOW("0000","放行: 执行后续流程，不受规则引擎影响"),
    TAKE_OVER("0001","接管: 后续流程受规则引擎影响"),
    ;

    private final String code;
    private final String info;
}
