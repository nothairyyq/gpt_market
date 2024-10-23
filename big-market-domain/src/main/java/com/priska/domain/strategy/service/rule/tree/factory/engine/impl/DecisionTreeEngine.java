package com.priska.domain.strategy.service.rule.tree.factory.engine.impl;

import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.priska.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.priska.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.priska.domain.strategy.model.valobj.RuleTreeVO;
import com.priska.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.priska.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description: 决策引擎实现类
 * @author: Priska
 * @create: 2024-10-16
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    //<规则节点的键，ILogicTreeNode 规则树节点的逻辑实现>
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }


    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId) {
        DefaultTreeFactory.StrategyAwardVO strategyAwardVO = null;

        //1. 获取根节点的ruleKey: nextNode
        String nextNode = ruleTreeVO.getTreeRootRuleNode();
        //2.获取整个树的节点
        //key:ruleKey, value: RuleTreeNode
        Map<String, RuleTreeNodeVO> treeNodeVOMap = ruleTreeVO.getTreeNodeMap();

        //3.根据根节点的ruleKey 在treeNodeVOMap获取根节点RuleTreeNodeVO
        RuleTreeNodeVO ruleTreeNodeVO = treeNodeVOMap.get(nextNode);
        //从起始节点开始遍历
        while(nextNode != null){
            //4. 获取当前节点的logicTreeNode
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(ruleTreeNodeVO.getRuleKey()); //ruleKey = “rule_lock”
            //5. 使用logicTreeNode的logic函数执行过滤操作并返回TreeActionEntity, 获取结果中的放行或接管结果
            DefaultTreeFactory.TreeActionEntity logicEntity = logicTreeNode.logic(userId,strategyId,awardId);
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckTypeVO();
            strategyAwardVO = logicEntity.getStrategyAwardVO();
            log.info("决策树引擎【{}】treeId:{} node:{} code:{}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), nextNode, ruleLogicCheckTypeVO.getCode());

            //6.获取下一个符合要求的节点
            nextNode = nextNode(ruleLogicCheckTypeVO.getCode(),ruleTreeNodeVO.getTreeNodeLineVOList());
            ruleTreeNodeVO = treeNodeVOMap.get(nextNode);
        }


        return strategyAwardVO;
    }

    //matterValue: 规则执行后的返回值，作为判断下一步的依据
    //treeNodeLineVOList: 当前节点的连线列表，决定了如何从当前节点移动到下一个节点
    public String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList){
        if(treeNodeLineVOList == null || treeNodeLineVOList.isEmpty()) return  null;
        //遍历当前节点的所有连线，调用decisionLogic检查是否满足条件。如果某条线的条件满足，返回下一个节点
        for (RuleTreeNodeLineVO nodeLineVO:treeNodeLineVOList){
            if (decisionLogic(matterValue, nodeLineVO)){
                return nodeLineVO.getRuleNodeTo();
            }
        }
        throw new RuntimeException("决策树引擎，nextNode计算失败，没有找到可以执行的节点");
    }

    //matterValue是规则执行结果（如ALLOW/TAKE_OVER）
    //nodeLine: 当前连线的限制条件，从当前节点到下一个节点的逻辑
    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine){
        switch (nodeLine.getRuleLimitType()){
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;

        }
    }
}
