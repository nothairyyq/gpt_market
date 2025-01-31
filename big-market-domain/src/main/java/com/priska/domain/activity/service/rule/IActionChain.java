package com.priska.domain.activity.service.rule;

import com.priska.domain.activity.model.entity.ActivityCountEntity;
import com.priska.domain.activity.model.entity.ActivityEntity;
import com.priska.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @program: IntelliJ IDEA
 * @description: 下单规则过滤责任链接口
 * @author: Priska
 * @create: 2025-01-31
 */
public interface IActionChain extends IActionChainArmory{

    /***
     * 下单sku落库处理逻辑 baseAction --> 落库ActIon
     * @param activitySkuEntity,activityEntity, activityCountEntity
     * @return boolean
     */
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
