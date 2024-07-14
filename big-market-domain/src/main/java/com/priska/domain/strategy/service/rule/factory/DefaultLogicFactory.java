package com.priska.domain.strategy.service.rule.factory;

import com.priska.domain.strategy.model.entity.RuleActionEntity;
import com.priska.domain.strategy.service.annotation.LogicStrategy;
import com.priska.domain.strategy.service.rule.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: IntelliJ IDEA
 * @description: 规则工厂
 * @author: Priska
 * @create: 2024-07-13
 */
@Service
public class DefaultLogicFactory {
    //定义了一个线程安全的ConcurrentHashMap， 用来存储逻辑模式和响应的ILogicFilter实现（黑名单和权重值）
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();
    //构造函数接受一个ILogicFilter实现的列表
    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            //使用Spring的AnnotationUtils工具查找逻辑过滤器类的注解
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WIGHT("rule_weight","【抽奖前规则】根据抽奖权重返回可抽奖范围KEY"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回"),

        ;

        private final String code;
        private final String info;

    }

}
