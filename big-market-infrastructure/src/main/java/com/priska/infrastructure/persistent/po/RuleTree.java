package com.priska.infrastructure.persistent.po;

import com.priska.domain.strategy.model.valobj.RuleTreeNodeVO;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树 根节点
 * @author: Priska
 * @create: 2024-10-18
 */
@Data
public class RuleTree {
    //自增ID
    private Long id;
    //规则树ID
    private String treeId;
    //规则树名称
    private String treeName;
    //规则树描述
    private String treeDesc;
    //规则树的根节点
    private String treeRootRuleKey;

    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
