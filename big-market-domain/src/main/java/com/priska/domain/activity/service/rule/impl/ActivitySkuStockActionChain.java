package com.priska.domain.activity.service.rule.impl;

import com.priska.domain.activity.model.entity.ActivityCountEntity;
import com.priska.domain.activity.model.entity.ActivityEntity;
import com.priska.domain.activity.model.entity.ActivitySkuEntity;
import com.priska.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

/**
 * @program: IntelliJ IDEA
 * @description: 商品库存规则责任链节点
 * @author: Priska
 * @create: 2025-01-31
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {
    /***
     * 下单sku落库处理逻辑 baseAction --> 落库ActIon
     *
     * @param activitySkuEntity
     * @param activityEntity
     * @param activityCountEntity
     * @return boolean
     */
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链--商品库存处理【校验&扣减】");
        return true;
    }
}
