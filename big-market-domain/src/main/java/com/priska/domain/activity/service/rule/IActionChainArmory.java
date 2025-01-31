package com.priska.domain.activity.service.rule;

/**
 * @program: IntelliJ IDEA
 * @description: 责任链装配接口 为Action责任链提供装配功能
 * @author: Priska
 * @create: 2025-01-31
 */
public interface IActionChainArmory {
    /***
     * 返回责任链的下一个节点，用于在责任链中内部传递，基本只用于rule下内部类
     */
    IActionChain next();

    /***
     * 用于将下一个责任链节点连接到当前节点，形成责任链的顺序结构，也只用于责任链内部
     * @param next IActionChain 责任链下一个节点
     * @return IActionChain 返回next 责任链下一个节点
     */
    IActionChain appendNext(IActionChain next);
}
