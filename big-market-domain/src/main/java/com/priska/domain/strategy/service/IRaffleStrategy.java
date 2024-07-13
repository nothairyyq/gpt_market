package com.priska.domain.strategy.service;

import com.priska.domain.strategy.model.entity.RaffleAwardEntity;
import com.priska.domain.strategy.model.entity.RaffleFactorEntity;

/*
* 抽奖策略接口
* */
public interface IRaffleStrategy {
    /*
    * 执行抽奖接口
    * 入参： 抽奖因子，执行抽奖计算，返回奖品信息
    * @param: raffleFactorEntity 抽奖因子实体对象，根据入参信息计算抽奖结果
    * @return: 返回奖品信息
    * */
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
