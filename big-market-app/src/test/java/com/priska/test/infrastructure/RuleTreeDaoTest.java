package com.priska.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.priska.infrastructure.persistent.dao.IRuleTreeDao;
import com.priska.infrastructure.persistent.dao.IRuleTreeNodeDao;
import com.priska.infrastructure.persistent.dao.IRuleTreeNodeLineDao;
import com.priska.infrastructure.persistent.po.RuleTree;
import com.priska.infrastructure.persistent.po.RuleTreeNode;
import com.priska.infrastructure.persistent.po.RuleTreeNodeLine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 测试规则树po
 * @author: Priska
 * @create: 2024-10-22
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTreeDaoTest {
    @Resource
    private IRuleTreeDao ruleTreeDao;
    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Test
    public void test_queryRuleTreeByTreeId(){
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId("tree_lock");
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId("tree_lock");
        List<RuleTree> ruleTree1 = ruleTreeDao.queryRuleTree();
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId("tree_lock");

        log.info("测试结果：{}", JSON.toJSONString(ruleTree));
        log.info("测试结果2：{}", JSON.toJSONString(ruleTree1));
        log.info("测试结果3：{}", JSON.toJSONString(ruleTreeNodes));
        log.info("测试结果4：{}", JSON.toJSONString(ruleTreeNodeLines));
    }
}
