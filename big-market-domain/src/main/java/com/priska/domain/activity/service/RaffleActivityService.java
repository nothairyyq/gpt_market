package com.priska.domain.activity.service;

import com.priska.domain.activity.model.aggregate.CreateOrderAggregate;
import com.priska.domain.activity.model.entity.*;
import com.priska.domain.activity.model.valobj.OrderStateVO;
import com.priska.domain.activity.repository.IActivityRepository;
import com.priska.domain.activity.service.rule.factory.DefaultActionChainFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        ActivityOrderEntity activityOrderEntity = ActivityOrderEntity.builder()
                .userId(skuRechargeEntity.getUserId())
                .sku(skuRechargeEntity.getSku())
                .activityId(activityEntity.getActivityId())
                .activityName(activityEntity.getActivityName())
                .strategyId(activityEntity.getStrategyId())
                .orderId(RandomStringUtils.randomNumeric(12))
                .orderTime(new Date())
                .totalCount(activityCountEntity.getTotalCount())
                .dayCount(activityCountEntity.getDayCount())
                .monthCount(activityCountEntity.getMonthCount())
                .state(OrderStateVO.completed)
                .outBusinessNo(skuRechargeEntity.getOutBusinessNo())
                .build();

        return CreateOrderAggregate.builder()
                .userId(activityOrderEntity.getUserId())
                .activityId(activityEntity.getActivityId())
                .totalCount(activityOrderEntity.getTotalCount())
                .dayCount(activityOrderEntity.getDayCount())
                .monthCount(activityOrderEntity.getMonthCount())
                .activityOrderEntity(activityOrderEntity)
                .build();
    }

    @Override
    protected void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        activityRepository.doSaveOrder(createOrderAggregate);
    }
}
