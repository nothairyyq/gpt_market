package com.priska.domain.activity.repository;

import com.priska.domain.activity.model.aggregate.CreateOrderAggregate;
import com.priska.domain.activity.model.entity.ActivityCountEntity;
import com.priska.domain.activity.model.entity.ActivityEntity;
import com.priska.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @program: IntelliJ IDEA
 * @description: 活动仓储接口
 * @param:
 * @return:
 * @author: Priska
 * @create: 2025-01-28
 */
public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);
    ActivityEntity queryRaffleActivityByActivityId(Long activityId);
    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);
    void doSaveOrder(CreateOrderAggregate createOrderAggregate);
}
