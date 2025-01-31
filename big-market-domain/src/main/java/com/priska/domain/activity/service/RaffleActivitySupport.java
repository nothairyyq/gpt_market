package com.priska.domain.activity.service;

import com.priska.domain.activity.model.entity.ActivityCountEntity;
import com.priska.domain.activity.model.entity.ActivityEntity;
import com.priska.domain.activity.model.entity.ActivitySkuEntity;
import com.priska.domain.activity.repository.IActivityRepository;
import com.priska.domain.activity.service.rule.factory.DefaultActionChainFactory;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动支持类
 * @author: Priska
 * @create: 2025-01-30
 */
public class RaffleActivitySupport {
    protected IActivityRepository activityRepository;

    protected DefaultActionChainFactory defaultActionChainFactory;

    public RaffleActivitySupport(IActivityRepository activityRepository, DefaultActionChainFactory defaultActionChainFactory){
        this.activityRepository = activityRepository;
        this.defaultActionChainFactory = defaultActionChainFactory;
    }

    public ActivitySkuEntity queryActivitySku(Long sku){
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId){
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId){
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}
