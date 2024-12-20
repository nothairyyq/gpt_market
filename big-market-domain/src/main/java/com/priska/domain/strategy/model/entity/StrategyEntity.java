package com.priska.domain.strategy.model.entity;

import com.priska.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyEntity {
    /*抽奖策略ID*/
    private Long strategyId;
    /*抽奖策略描述*/
    private String strategyDesc;
    /*抽奖规则模型 rule_weight, rule_blacklist*/
    private String ruleModels;

    public String[] ruleModels(){
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight(){
        String[] ruleModels = this.ruleModels();
        if (ruleModels == null || ruleModels.length == 0) {
            return null; // 这里可以根据需求返回默认值或者 null
        }
        for(String ruleModel : ruleModels){
            if ("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }
}
