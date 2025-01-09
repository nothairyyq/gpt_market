package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: IntelliJ IDEA
 * @description:
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-12-27
 */
@Mapper
public interface IRaffleActivityDao {
    RaffleActivity queryRaffleActivityByActivityId(Long activityId);
}
