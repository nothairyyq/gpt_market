package com.priska.test.domain;

import com.alibaba.fastjson.JSON;
import com.priska.domain.strategy.model.valobj.*;
import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.priska.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: IntelliJ IDEA
 * @description: 规则树测试
 * @author: Priska
 * @create: 2024-10-17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogicTreeTest {
    @Resource
    private DefaultTreeFactory defaultTreeFactory;

    //rule_lock -->(接管)rule_luck_award
    //          -->（放行）rule_stock --> rule_luck_award
    @Test
    public void test_tree_rule(){
        //初始化三个树节点，rule_lock, rule_luck_award, rule_stock
        RuleTreeNodeVO rule_lock = RuleTreeNodeVO.builder()
                .treeId("100001")
                .ruleKey("rule_lock")
                .ruleDesc("次数锁，限定用户完成N次抽奖后解锁奖品")
                .ruleValue("1")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>(){{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVO.TAKE_OVER)
                            .build());
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100001")
                            .ruleNodeFrom("rule_lock")
                            .ruleNodeTo("rule_stock")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVO.ALLOW)
                            .build());
                }})
                .build();

        RuleTreeNodeVO rule_luck_award = RuleTreeNodeVO.builder()
                .treeId("100001")
                .ruleKey("rule_luck_award")
                .ruleDesc("兜底奖励")
                .ruleValue("1")
                .treeNodeLineVOList(null)
                .build();
        RuleTreeNodeVO rule_stock = RuleTreeNodeVO.builder()
                .treeId("100001")
                .ruleKey("rule_stock")
                .ruleDesc("库存处理规则")
                .ruleValue(null)
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>(){{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId("100001")
                            .ruleNodeFrom("rule_stock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVO.TAKE_OVER)
                            .build());
                }})
                .build();

        //构建整个树
        RuleTreeVO ruleTreeVO = new RuleTreeVO();
        ruleTreeVO.setTreeId("100001");
        ruleTreeVO.setTreeName("决策树规则测试");
        ruleTreeVO.setTreeDesc("决策树测试");
        ruleTreeVO.setTreeRootRuleNode("rule_lock");
        ruleTreeVO.setTreeNodeMap(new HashMap<String,RuleTreeNodeVO>(){{
            put("rule_lock",rule_lock);
            put("rule_stock",rule_stock);
            put("rule_luck_award",rule_luck_award);
        }});

        //构建树引擎, 将树传入
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);

        DefaultTreeFactory.StrategyAwardVO data = treeEngine.process("priska",100001L,100);
        log.info("测试结果：{}", JSON.toJSONString(data));
    }
}
