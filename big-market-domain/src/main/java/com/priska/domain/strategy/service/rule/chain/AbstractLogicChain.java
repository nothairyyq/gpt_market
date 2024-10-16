package com.priska.domain.strategy.service.rule.chain;

import com.priska.domain.strategy.service.rule.chain.ILogicChain;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖策略责任链抽象类
 * @author: Priska
 * @create: 2024-10-16
 */
public abstract class AbstractLogicChain implements ILogicChain {
//处理责任链的顺序，并定义了每个节点的抽象逻辑
    //next: 存储当前责任链的下一个节点
    //appendNext: 将下一个责任节点 连接到当前节点
    //ruleModel: 抽象方法，子类必须实现具体的规则逻辑
    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    protected abstract String ruleModel();
}
