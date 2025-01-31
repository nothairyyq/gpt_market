package com.priska.domain.activity.service;

import com.priska.domain.activity.model.aggregate.CreateOrderAggregate;
import com.priska.domain.activity.model.entity.ActivityCountEntity;
import com.priska.domain.activity.model.entity.ActivityEntity;
import com.priska.domain.activity.model.entity.ActivitySkuEntity;
import com.priska.domain.activity.model.entity.SkuRechargeEntity;
import com.priska.domain.activity.repository.IActivityRepository;
import com.priska.domain.activity.service.rule.factory.DefaultActionChainFactory;
import org.springframework.stereotype.Service;

/**
 * @program: IntelliJ IDEA
 * @description:
 * @author: Priska
 * @create: 2025-01-29
 */
@Service
public class RaffleActivityService extends AbstractRaffleActivity {

    public RaffleActivityService(IActivityRepository activityRepository, DefaultActionChainFactory defaultActionChainFactory) {
        super(activityRepository, defaultActionChainFactory);
    }

    @Override
    protected CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        return null;
    }

    @Override
    protected void doSaveOrder(CreateOrderAggregate createOrderAggregate) {

    }
}
