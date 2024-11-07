package com.priska.domain.strategy.service;

import com.priska.domain.strategy.model.entity.RaffleAwardEntity;
import com.priska.domain.strategy.model.entity.RaffleFactorEntity;
import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.domain.strategy.service.armory.IStrategyDispatch;
import com.priska.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.priska.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
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
public abstract class AbstractRaffleStrategy implements IRaffleStrategy{
    //策略仓储服务 -》 domain层类比一个大厨，仓储层提供米面粮油
    protected IStrategyRepository repository;
    //策略调度服务 -》 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;
    //抽奖责任链 - 》 从抽奖规则过滤中，将前置规则使用责任链模式处理
    protected final DefaultChainFactory defaultChainFactory;
    //抽奖决策树 - 》 负责抽奖中到抽奖后的规则过滤，如抽奖到A奖品ID，之后要做次数的判断和库存的扣减等。
    protected final DefaultTreeFactory defaultTreeFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory){
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (strategyId == null || StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //2. 责任链抽奖计算，初步拿到奖品ID,后续根据奖品id进行规则树过滤。
        // 黑名单，权重这种非默认抽奖应该直接返回结果而不参与后续规则树过滤
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        if(!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())){
            // TODO awardConfig 暂时为空。黑名单指定积分奖品，后续需要在库表中配置上对应的1积分值，并获取到。
            return buildRaffleAwardEntity(strategyId, chainStrategyAwardVO.getAwardId(), null);
        }

        //3.根据奖品ID 进行规则树抽奖过滤
        // 会进入一个多叉树的逻辑 规则树中 可能会经过抽奖次数判断rule_lock, 库存判断 rule_stock，和兜底返回rule_luck_award 返回最终的可获得奖品信息
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId,strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());


        //4.返回抽奖结果
        return buildRaffleAwardEntity(strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());
    }

    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId, Integer awardId, String awardConfig){
        StrategyAwardEntity strategyAward = repository.queryStrategyAwardEntity(strategyId,awardId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .awardConfig(awardConfig)
                .sort(strategyAward.getSort())
                .build();
    }

    //protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
    //protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
    /*
    * 计算抽奖结果，责任链抽象方法，对应之前的抽奖前过滤
    *
    * @param userId     用户id
    * @param strategyId 策略id
    * @return 奖品VO(awardId,logicModel)
    * */
    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    /*
     * 根据抽奖结果进行过滤，决策树抽象方法
     *
     * @param userId     用户id
     * @param strategyId 策略id
     * @return 奖品VO(awardId,awardRuleValue)
     * */
    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);
}
