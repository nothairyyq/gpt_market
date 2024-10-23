package com.priska.infrastructure.persistent.po;

import com.priska.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树节点
 * @author: Priska
 * @create: 2024-10-18
 */
@Data
public class RuleTreeNode {
    private Long id;
    //规则树ID
    private String treeId;
    //规则key
    private String ruleKey;
    //规则描述
    private String ruleDesc;
    //规则比值
    private String ruleValue;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}
