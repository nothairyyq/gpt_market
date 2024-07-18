package com.priska.domain.strategy.model.valobj;

import com.priska.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.priska.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description:抽奖策略奖品规则值对象，没有唯一ID, 仅限于从数据库中查询对象
 * @author: Priska
 * @create: 2024-07-18
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

    //获取抽奖中规则
    //rule_model: rule_random, rule_luck_award, rule_lock
    public String[] raffleCenterRuleModelList(){
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues){
            if (DefaultLogicFactory.isCenter(ruleModelValue)){
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }

}