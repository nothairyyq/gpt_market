package com.priska.domain.strategy.service.rule.chain;



/**
 * @program: IntelliJ IDEA
 * @description: 为责任链提供装配功能
 * @author: Priska
 * @create: 2024-10-16
 */
public interface ILogicChainArmory {
    /***
     * 返回责任链的下一个节点，用于在责任链中内部传递，基本只用于rule下内部类
     */
    ILogicChain next();

    /***
     * 用于将下一个责任链节点连接到当前节点，形成责任链的顺序结构，也只用于责任链内部
     * @param next ILogicChain 责任链下一个节点
     * @return ILogicChain 返回next 责任链下一个节点
     */
    ILogicChain appendNext(ILogicChain next);
}
