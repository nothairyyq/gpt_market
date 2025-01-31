package com.priska.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.priska.domain.activity.model.aggregate.CreateOrderAggregate;
import com.priska.domain.activity.model.entity.*;
import com.priska.domain.activity.repository.IActivityRepository;
import com.priska.domain.activity.service.rule.IActionChain;
import com.priska.domain.activity.service.rule.factory.DefaultActionChainFactory;
import com.priska.types.enums.ResponseCode;
import com.priska.types.exception.AppException;
import com.priska.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动抽象类，标准抽奖流程
 * @author: Priska
 * @create: 2025-01-29
 */
@Slf4j
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder{

    public AbstractRaffleActivity(IActivityRepository activityRepository, DefaultActionChainFactory defaultActionChainFactory){
        super(activityRepository, defaultActionChainFactory);
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
        ActivitySkuEntity activitySkuEntity = queryActivitySku(activityShopCartEntity.getSku());
        //2. 通过activity sku的activity id查询activity entity  活动信息
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //3. 通过activity sku的activity count id查询activity count entity 活动次数信息，也就是用户可以在这个活动上参与的次数
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));
        return ActivityOrderEntity.builder().build();
    }

    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        //1. 校验参数， 检查userId, sku, outBusinessNo 是否都存在
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (StringUtils.isBlank(userId) || sku == null || StringUtils.isBlank(outBusinessNo)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        //2. 查询基础信息：ActivitySkuEntity, ActivityEntity, ActivityCountEntity
        //2.1 用sku查ActivitySkuEntity
        ActivitySkuEntity activitySkuEntity = queryActivitySku(skuRechargeEntity.getSku());
        //2.2 用activityId查activityEntity
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //2.3用activityCountId查activityCountEntity
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        //3. 活动动作规则校验 获取责任链头节点 并获得责任链结果
        IActionChain actionChain = defaultActionChainFactory.openActionChain();
        boolean success = actionChain.action(activitySkuEntity,activityEntity,activityCountEntity);

        //4. 构建订单聚合对象，使用之前查到的信息
        CreateOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);
        //5. 保存订单聚合对象
        doSaveOrder(createOrderAggregate);
        //6. 返回单号
        return createOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
    protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);
}
