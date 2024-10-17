package com.priska.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description: 决策树 的树根信息。规则树对象
 * @author: Priska
 * @create: 2024-10-16
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RuleTreeVO {
    //规则树ID
    private String treeId;
    //规则树名称
    private String treeName;
    //规则树描述
    private String treeDesc;
    //规则树的根节点
    private String treeRootRuleNode;

    //规则节点
    //key:ruleKey, value: RuleTreeNode
    private Map<String, RuleTreeNodeVO> treeNodeMap;
}
