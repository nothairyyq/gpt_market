package com.priska.trigger.http;

import com.alibaba.fastjson2.JSON;
import com.priska.domain.strategy.service.IRaffleStrategy;
import com.priska.domain.strategy.service.armory.IStrategyArmory;
import com.priska.trigger.api.IRaffleService;
import com.priska.trigger.api.dto.RaffleAwardListRequestDTO;
import com.priska.trigger.api.dto.RaffleAwardListResponseDTO;
import com.priska.trigger.api.dto.RaffleRequestDTO;
import com.priska.trigger.api.dto.RaffleResponseDTO;
import com.priska.types.enums.ResponseCode;
import com.priska.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description: 营销抽奖服务控制层
 * @author: Priska
 * @create: 2024-11-06
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}") //占位符，配置放在了app-application-dev.yml中，开发阶段设置为*不限制
@RequestMapping("/api/v1/raffle/")
public class RaffleController implements IRaffleService {

    @Resource
    private IStrategyArmory strategyArmory;

    /**
     * 策略装配接口
     *<a href="http://localhost:8091/api/v1/raffle/strategy_armory">/api/v1/raffle/strategy_armory</a>
     *
     * @param strategyId
     * @return 装配结果 Response实体
     */
    @RequestMapping(value = "strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(Long strategyId) {
        try{
            log.info("抽奖策略装配开始 strategyID:{}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("抽奖策略装配结束 strategyID: {} response:{}",strategyId, JSON.toJSON(response));
            return response;
        }catch (Exception e){
            log.error("抽奖策略装配失败 strategyID:{}",strategyId,e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 查询抽奖奖品列表的接口
     *
     * @param requestDTO 抽奖奖品列表查询请求参数
     * @return RaffleAwardListResponseDTO 奖品列表响应实体{awardId, title, subtitle, sort}
     */
    @Override
    public Response<RaffleAwardListResponseDTO> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO) {
        return null;
    }

    /**
     * 随机抽奖接口
     *
     * @param requestDTO 随机抽奖请求参数
     * @return RaffleResponseDTO 随机抽奖结果{awardId, awardIndex}
     */
    @Override
    public Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO) {
        return null;
    }
}
