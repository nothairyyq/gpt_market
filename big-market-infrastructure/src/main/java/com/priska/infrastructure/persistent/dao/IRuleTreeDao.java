package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树DAO
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-10-18
 */
@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByTreeId(String treeId);
    List<RuleTree> queryRuleTree();
}
