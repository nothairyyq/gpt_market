package com.priska.domain.strategy.model.valobj;

import com.priska.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.priska.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖策略规则 的规则值对象，没有唯一ID,仅限于从数据库中查询对象
 * @author: Priska
 * @create: 2024-10-13
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {
    private String ruleModels;
}
