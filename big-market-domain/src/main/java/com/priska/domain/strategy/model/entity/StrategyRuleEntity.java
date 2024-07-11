package com.priska.domain.strategy.model.entity;

import com.priska.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
*策略规则实体
* **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {
    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖奖品ID【规则类型为策略，则不需要奖品ID】*/
    private Integer awardId;
    /*抽象规则类型；1-策略规则、2-奖品规则*/
    private Integer ruleType;
    /*抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】*/
    private String ruleModel;
    /*抽奖规则比值*/
    private String ruleValue;
    /*抽奖规则描述*/
    private String ruleDesc;


    //获取策略规则数据中的权重值
    //数据案例：4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
    //resultMap = key: 4000:102,103.... value: [102,103,104...]
    public Map<String, List<Integer>> getRuleWeightValues(){
        if (!"rule_weight".equals(ruleModel)) return null;
        //先按照空格分开，ruleValueGroups =[[4000:102,103...],[5000:102,103...]]
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> resultMap = new HashMap<>();
        for (String ruleValueGroup : ruleValueGroups){
            if (ruleValueGroup == null || ruleValueGroup.isEmpty()){
                return resultMap;
            }
            //2. 按照冒号分开，parts=[[4000],[102,103,104...]]
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if (parts.length != 2){
                throw new IllegalArgumentException("策略规则值输入不正确的格式"+ruleValueGroup);
            }
            //3. 将parts中的后半部分按照逗号分开，valueStrings = [102,103,...]
            String[] valueStrings = parts[1].split(Constants.SPLIT);
            List<Integer> values = new ArrayList<>();
            for (String valueString : valueStrings){
                values.add(Integer.parseInt(valueString));
            }
            resultMap.put(ruleValueGroup, values);
        }
        return resultMap;
    }

}
