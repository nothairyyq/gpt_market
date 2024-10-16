package com.priska.domain.strategy.service.rule.chain.factory;

import com.priska.domain.strategy.model.entity.StrategyEntity;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description: 责任链工厂类
 * @author: Priska
 * @create: 2024-10-16
 */
@Service
public class DefaultChainFactory {

    //保存所有可用的责任链节点
    //key是每个责任链节点的名称`rule_blacklist`,`rule_weight`; value是对应的ILogicChain实例
    //通过扫描@Component的类，按照指定名称将这些类实例化Bean并注册到容器中
    private final Map<String, ILogicChain> logicChainGroup; //final关键字是保证group初始化之后不会被修改，保证不可变性
    protected IStrategyRepository repository;//protected 允许当前类及子类访问repository,限制了外部非子类的访问权限

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId){
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();
        //如果数据库中对应的策略没有配置ruleModels,直接装填一个默认的责任链
        if (ruleModels == null || ruleModels.length == 0)
            return logicChainGroup.get("default");

        //// 按照配置顺序装填用户配置的责任链；rule_blacklist、rule_weight
        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextChain = logicChainGroup.get(ruleModels[i]);
            current = current.appendNext(nextChain);
        }

        //把默认责任链装填到最后
        current.appendNext(logicChainGroup.get("default"));

        return logicChain;
    }
}
