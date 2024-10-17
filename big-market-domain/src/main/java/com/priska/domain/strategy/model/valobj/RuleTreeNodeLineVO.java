package com.priska.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树节点之间的线对象，用于连接form->to的节点链路关系
 * @author: Priska
 * @create: 2024-10-16
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RuleTreeNodeLineVO {
    //规则树ID
    private String treeId;
    //规则key 节点from
    private String ruleNodeFrom;
    //规则key 节点to
    private String ruleNodeTo;

    //限定类型
    private RuleLimitTypeVO ruleLimitType;
    //限定值
    private RuleLogicCheckTypeVO ruleLimitValue;
}
