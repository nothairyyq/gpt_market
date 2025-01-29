package com.priska.infrastructure.persistent.repository;

import com.priska.domain.activity.model.entity.ActivityCountEntity;
import com.priska.domain.activity.model.entity.ActivityEntity;
import com.priska.domain.activity.model.entity.ActivitySkuEntity;
import com.priska.domain.activity.model.valobj.ActivityStateVO;
import com.priska.domain.activity.repository.IActivityRepository;
import com.priska.infrastructure.persistent.dao.IRaffleActivityCountDao;
import com.priska.infrastructure.persistent.dao.IRaffleActivityDao;
import com.priska.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import com.priska.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import com.priska.infrastructure.persistent.po.RaffleActivity;
import com.priska.infrastructure.persistent.po.RaffleActivityCount;
import com.priska.infrastructure.persistent.po.RaffleActivitySku;
import com.priska.infrastructure.persistent.redis.IRedisService;
import com.priska.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description: 活动仓储实现类
 * @author: Priska
 * @create: 2025-01-29
 */
@Repository
public class ActivityRepository implements IActivityRepository {
    @Resource
    private IRedisService redisService;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        //先从redis中查
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY+activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (activityEntity != null) return activityEntity;
        //redis中没有从mysql中查
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .strategyId(raffleActivity.getStrategyId())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
        redisService.setValue(cacheKey,activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        //从redis中查
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY+activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if(activityCountEntity!=null) return activityCountEntity;
        //从mysql中查
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .totalCount(raffleActivityCount.getTotalCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }


}
