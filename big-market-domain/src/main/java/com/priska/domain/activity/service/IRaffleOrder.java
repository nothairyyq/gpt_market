package com.priska.domain.activity.service;

import com.priska.domain.activity.model.entity.ActivityOrderEntity;
import com.priska.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动订单接口
 * @author: Priska
 * @create: 2025-01-29
 */
public interface IRaffleOrder {

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *
     * @param activityShopCartEntity 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
