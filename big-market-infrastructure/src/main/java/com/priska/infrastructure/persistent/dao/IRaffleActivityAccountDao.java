package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: IntelliJ IDEA
 * @description: 活动账户DAO
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-12-27
 */
@Mapper
public interface IRaffleActivityAccountDao {
    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);
}
