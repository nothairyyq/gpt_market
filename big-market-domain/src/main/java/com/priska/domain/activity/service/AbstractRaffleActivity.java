package com.priska.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.priska.domain.activity.model.entity.*;
import com.priska.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动抽象类，标准抽奖流程
 * @author: Priska
 * @create: 2025-01-29
 */
@Slf4j
public class AbstractRaffleActivity implements IRaffleOrder{

    protected IActivityRepository activityRepository;

    public AbstractRaffleActivity(IActivityRepository activityRepository){
        this.activityRepository = activityRepository;
    }

    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *
     * @param activityShopCartEntity 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        //1. 通过sku查询activity sku 信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        //2. 通过activity sku的activity id查询activity entity  活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //3. 通过activity sku的activity count id查询activity count entity 活动次数信息，也就是用户可以在这个活动上参与的次数
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));
        return ActivityOrderEntity.builder().build();
    }
}
