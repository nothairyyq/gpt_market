package com.priska.trigger.api;

import com.priska.trigger.api.dto.RaffleAwardListRequestDTO;
import com.priska.trigger.api.dto.RaffleAwardListResponseDTO;
import com.priska.trigger.api.dto.RaffleRequestDTO;
import com.priska.trigger.api.dto.RaffleResponseDTO;
import com.priska.types.model.Response;

import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖服务接口
 * @param:
 * @return:
 * @author: Priska
 * @create: 2024-11-06
 */
public interface IRaffleService {

    /**
     * 策略装配接口
     * @param strategyId
     * @return 装配结果 Response实体
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询抽奖奖品列表的接口
     * @param requestDTO 抽奖奖品列表查询请求参数
     * @return RaffleAwardListResponseDTO 奖品列表响应实体{awardId, title, subtitle, sort}
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /**
     * 随机抽奖接口
     * @param requestDTO 随机抽奖请求参数
     * @return RaffleResponseDTO 随机抽奖结果{awardId, awardIndex}
     */
    Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO);

}
