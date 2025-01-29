package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.RaffleActivityCount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动次数Dao
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-12-27
 */
@Mapper
public interface IRaffleActivityCountDao {
    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
