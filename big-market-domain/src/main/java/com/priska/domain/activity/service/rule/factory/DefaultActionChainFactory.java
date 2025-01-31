package com.priska.domain.activity.service.rule.factory;

import com.priska.domain.activity.service.rule.IActionChain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: IntelliJ IDEA
 * @description: 责任链工厂
 * @author: Priska
 * @create: 2025-01-31
 */
@Service
public class DefaultActionChainFactory {
    private final IActionChain actionChain;

    /**
     * 1. 通过构造函数注入。
     * 2. Spring 可以自动注入 IActionChain 接口实现类到 map 对象中，key 就是 bean 的名字。
     * 3. 活动下单动作的责任链是固定的，所以直接在构造函数中组装即可。
     */
    public DefaultActionChainFactory(Map<String, IActionChain> actionChainGroup){
        actionChain = actionChainGroup.get(ActionModel.activity_base_action.code);
        actionChain.appendNext(actionChainGroup.get(ActionModel.activity_sku_stock_action.code));
    }

    public IActionChain openActionChain(){
        return this.actionChain;
    }




    @AllArgsConstructor
    public enum ActionModel{
        activity_base_action("activity_base_action","活动库存，时间校验"),
        activity_sku_stock_action("activity_sku_stock_action", "活动sku库存"),
        ;
        private final String code;
        private final String info;
    }
}
