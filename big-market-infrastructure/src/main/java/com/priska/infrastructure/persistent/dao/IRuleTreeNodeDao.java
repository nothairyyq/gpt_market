package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树节点Dao
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-10-18
 */
@Mapper
public interface IRuleTreeNodeDao {
    List<RuleTreeNode> queryRuleTreeNodeListByTreeId(String treeId);
}
