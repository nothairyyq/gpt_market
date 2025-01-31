package com.priska.domain.activity.service.rule;

/**
 * @program: IntelliJ IDEA
 * @description: 下单规则责任链抽象类
 * @author: Priska
 * @create: 2025-01-31
 */
public abstract class AbstractActionChain implements IActionChain{
    //处理责任链的顺序，并定义了每个节点的抽象逻辑, action函数由具体的子类实现
    //next: 存储当前责任链的下一个节点
    //appendNext: 将下一个责任节点 连接到当前节点
    private IActionChain next;

    /***
     * 返回责任链的下一个节点，用于在责任链中内部传递，基本只用于rule下内部类
     */
    @Override
    public IActionChain next() {
        return next;
    }

    /***
     * 用于将下一个责任链节点连接到当前节点，形成责任链的顺序结构，也只用于责任链内部
     * @param next IActionChain 责任链下一个节点
     * @return IActionChain 返回next 责任链下一个节点
     */
    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }
}
