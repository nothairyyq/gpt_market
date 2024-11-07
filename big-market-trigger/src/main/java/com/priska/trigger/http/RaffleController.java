package com.priska.trigger.http;

import com.alibaba.fastjson2.JSON;
import com.priska.domain.strategy.model.entity.RaffleAwardEntity;
import com.priska.domain.strategy.model.entity.RaffleFactorEntity;
import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.service.IRaffleAward;
import com.priska.domain.strategy.service.IRaffleStrategy;
import com.priska.domain.strategy.service.armory.IStrategyArmory;
import com.priska.trigger.api.IRaffleService;
import com.priska.trigger.api.dto.RaffleAwardListRequestDTO;
import com.priska.trigger.api.dto.RaffleAwardListResponseDTO;
import com.priska.trigger.api.dto.RaffleRequestDTO;
import com.priska.trigger.api.dto.RaffleResponseDTO;
import com.priska.types.enums.ResponseCode;
import com.priska.types.exception.AppException;
import com.priska.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    @Resource
    private IRaffleAward raffleAward;
    @Resource
    private IRaffleStrategy raffleStrategy;

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
            log.info("抽奖策略装配结束 strategyID: {} response:{}",strategyId, JSON.toJSONString(response));
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
     * <a href="http://localhost:8091/api/v1/raffle/query_raffle_award_list">/api/v1/raffle/query_raffle_award_list</a>
     *
     * @param requestDTO 抽奖奖品列表查询请求参数
     * @return RaffleAwardListResponseDTO 奖品列表响应实体{awardId, title, subtitle, sort}
     */
    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.GET)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO) {
        try {
            log.info("查询抽奖奖品列表开始 strategyID：{}", requestDTO.getStrategyId());

            List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStrategyAwardList(requestDTO.getStrategyId());
            List<RaffleAwardListResponseDTO> raffleAwardListResponseDTOList = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities){
                raffleAwardListResponseDTOList.add(RaffleAwardListResponseDTO.builder()
                                .awardId(strategyAwardEntity.getAwardId())
                                .awardTitle(strategyAwardEntity.getAwardTitle())
                                .awardSubtitle(strategyAwardEntity.getAwardSubtitle())
                                .sort(strategyAwardEntity.getSort())
                                .build());
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .info(ResponseCode.SUCCESS.getInfo())
                    .code(ResponseCode.SUCCESS.getCode())
                    .data(raffleAwardListResponseDTOList)
                    .build();
            log.info("查询抽奖奖品列表结束 strategyID: {} response: {}", requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        }catch (Exception e){
            log.error("查询抽奖奖品列表配置失败 strategyId：{}", requestDTO.getStrategyId(), e);
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 随机抽奖接口
     *<a href="http://localhost:8091/api/v1/raffle/random_raffle">/api/v1/raffle/random_raffle</a>
     * @param requestDTO 随机抽奖请求参数
     * @return RaffleResponseDTO 随机抽奖结果{awardId, awardIndex}
     */
    @RequestMapping(value = "random_raffle", method = RequestMethod.POST)
    @Override
    public Response<RaffleResponseDTO> randomRaffle(@RequestBody RaffleRequestDTO requestDTO) {

        try {
            log.info("随机抽奖开始 strategyID: {}", requestDTO.getStrategyId());
            //调用抽奖接口
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId("system")
                    .strategyId(requestDTO.getStrategyId())
                    .build());
            //实体类-->DTO-->结果实体
            Response<RaffleResponseDTO> response = Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("随机抽奖完成 strategyID: {} response: {}",requestDTO.getStrategyId(), response);
            return response;
        }catch (AppException e){
            log.error("随机抽奖失败 strategyID：{} {}", requestDTO.getStrategyId(), e.getInfo());
            return Response.<RaffleResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }catch (Exception e){
            log.info("随机抽奖失败 strategyID:{}", requestDTO.getStrategyId(), e);
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
