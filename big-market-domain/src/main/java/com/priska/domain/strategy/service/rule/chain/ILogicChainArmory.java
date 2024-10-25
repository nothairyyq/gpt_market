package com.priska.domain.strategy.service.rule.chain;



/**
 * @program: IntelliJ IDEA
 * @description: 为责任链提供装配功能
 * @author: Priska
 * @create: 2024-10-16
 */
public interface ILogicChainArmory {
    //返回责任链的下一个节点，用于在责任链中传递
    ILogicChain next();
    //将下一个责任链节点连接到当前节点，形成责任链的顺序结构
    ILogicChain appendNext(ILogicChain next);
}
