package com.priska.domain.strategy.service.raffle;

import com.priska.domain.strategy.model.entity.RaffleAwardEntity;
import com.priska.domain.strategy.model.entity.RaffleFactorEntity;
import com.priska.domain.strategy.service.IRaffleStrategy;

/**
 * @program: IntelliJ IDEA
 * @description: 抽象抽奖策略类，定义抽奖的标准流程
 * @author: Priska
 * @create: 2024-07-13
 */
public class AbstractRaffleStrategy implements IRaffleStrategy {
    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        return null;
    }
}
