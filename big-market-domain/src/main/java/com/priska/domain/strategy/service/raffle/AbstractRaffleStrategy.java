package com.priska.domain.strategy.service.raffle;

import com.priska.domain.strategy.model.entity.RaffleAwardEntity;
import com.priska.domain.strategy.model.entity.RaffleFactorEntity;
import com.priska.domain.strategy.model.entity.RuleActionEntity;
import com.priska.domain.strategy.model.entity.StrategyEntity;
import com.priska.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.priska.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.IRaffleStrategy;
import com.priska.domain.strategy.service.armory.IStrategyDispatch;
import com.priska.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.priska.types.enums.ResponseCode;
import com.priska.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: IntelliJ IDEA
 * @description: 抽象抽奖策略类，定义抽奖的标准流程
 * @author: Priska
 * @create: 2024-07-13
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {
    //策略仓储服务 -》 domain层类比一个大厨，仓储层提供米面粮油
    protected IStrategyRepository repository;
    //策略调度服务 -》 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch){
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (strategyId == null || StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //2. 策略查询
        //根据传入的抽奖因子实体中的策略ID查询策略表或redis缓存中，的策略实体(包括策略id，策略描述，rule_models)
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);

        //3. 抽奖前的规则过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())){
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())){
                //黑名单返回固定奖品id
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
                //根据权重信息返回的进行抽奖
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

        //4. 默认抽奖流程
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);


        //5.拿到奖品Id后，需要判断该奖品是否需要rule_lock规则。
        //抽奖中：拿到奖品ID时，过滤规则。 抽奖后：扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底抽奖积分奖品
        //根据拿到的awardID在仓库层像策略奖品表中获取rule_models字段并植入VO对象中
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVo(strategyId, awardId);

        //6.抽奖中的规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .strategyId(strategyId)
                .userId(userId)
                .awardId(awardId)
                .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中将中规则拦截，发放rule_luck_award")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);


}
