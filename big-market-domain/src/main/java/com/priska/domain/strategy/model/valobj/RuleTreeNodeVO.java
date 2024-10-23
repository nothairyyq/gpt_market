package com.priska.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树节点对象. 通过节点组合出需要的规则树
 * @author: Priska
 * @create: 2024-10-16
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RuleTreeNodeVO {

    //规则树ID
    private String treeId;
    //规则key. rule_lock, rule_luck_award, rule_stock
    private String ruleKey;
    //规则描述
    private String ruleDesc;
    //规则比值
    private String ruleValue;

    //规则连线
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;

}
