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
import com.priska.domain.strategy.service.rule.chain.ILogicChain;
import com.priska.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.priska.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
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
    //抽奖责任链 - 》 从抽奖规则过滤中，将前置规则使用责任链模式处理
    private final DefaultChainFactory defaultChainFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch,DefaultChainFactory defaultChainFactory){
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (strategyId == null || StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 获取抽奖责任链 - 前置规则的责任链处理
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);

        //3.通过责任链获得奖品id
        Integer awardId = logicChain.logic(userId, strategyId);

//        //2. 策略查询
//        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
//
//        //3. 抽奖前的规则过滤
//        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder()
//                .userId(userId)
//                .strategyId(strategyId)
//                .build(), strategy.ruleModels());
//        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())){
//            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())){
//                //黑名单返回固定奖品id
//                return RaffleAwardEntity.builder()
//                        .awardId(ruleActionEntity.getData().getAwardId())
//                        .build();
//            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
//                //根据权重信息返回的进行抽奖
//                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
//                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
//                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
//                return RaffleAwardEntity.builder()
//                        .awardId(awardId)
//                        .build();
//            }
//        }
//
//        //4. 默认抽奖流程
//        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

        //4.拿到奖品id后，查询对应的奖品规则
        //抽奖中：awardId 对应的 ruleModel有可能的rule_lock和 rule_luck_award进行次数规则过滤
        //抽奖后：扣减完奖品库存后过滤，抽奖中拦截和无库存走兜底奖品
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        log.info("拿到strategyawardRuleModel:{}",strategyAwardRuleModelVO.getRuleModels());

        //5. 抽奖中 - 规则过滤
        //将只有是抽奖中的规则传入doCheckRaffleCenter函数中
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build(),strategyAwardRuleModelVO.raffleCenterRuleModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
            log.info("[临时日志]：抽奖中 规则过滤，用户抽奖次数小于ruleValue,走rule_luck_award过滤规则");
            return RaffleAwardEntity.builder()
                    .awardDesc("用户抽奖次数小于ruleValue,走rule_luck_award过滤规则")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    //protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}
