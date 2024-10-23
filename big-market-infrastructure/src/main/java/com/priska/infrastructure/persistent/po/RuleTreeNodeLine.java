package com.priska.infrastructure.persistent.po;

import com.priska.domain.strategy.model.valobj.RuleLimitTypeVO;
import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.Data;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树节点之间的连线
 * @author: Priska
 * @create: 2024-10-18
 */
@Data
public class RuleTreeNodeLine {
    private Long id;
    //规则树ID
    private String treeId;
    //规则key 节点from
    private String ruleNodeFrom;
    //规则key 节点to
    private String ruleNodeTo;

    //限定类型
    private String ruleLimitType;
    //限定值
    private String ruleLimitValue;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}
