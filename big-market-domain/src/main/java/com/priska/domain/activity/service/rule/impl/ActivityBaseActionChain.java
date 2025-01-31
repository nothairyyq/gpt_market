package com.priska.domain.activity.service.rule.impl;

import com.priska.domain.activity.model.entity.ActivityCountEntity;
import com.priska.domain.activity.model.entity.ActivityEntity;
import com.priska.domain.activity.model.entity.ActivitySkuEntity;
import com.priska.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: IntelliJ IDEA
 * @description: 活动规则基础过滤：日期，状态等
 * @author: Priska
 * @create: 2025-01-31
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
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
        log.info("活动规则责任链--基础信息【有效期，状态】校验开始");
        return next().action(activitySkuEntity,activityEntity,activityCountEntity);
    }
}
