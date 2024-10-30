package com.priska.infrastructure.persistent.repository;

import com.alibaba.fastjson.JSON;
import com.priska.domain.strategy.model.entity.StrategyAwardEntity;
import com.priska.domain.strategy.model.entity.StrategyEntity;
import com.priska.domain.strategy.model.entity.StrategyRuleEntity;
import com.priska.domain.strategy.model.valobj.*;
import com.priska.domain.strategy.repository.IStrategyRepository;
import com.priska.infrastructure.persistent.dao.*;
import com.priska.infrastructure.persistent.po.*;
import com.priska.infrastructure.persistent.redis.IRedisService;
import com.priska.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
* 实现策略仓储接口
* */
@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;
    @Resource
    private IRuleTreeDao ruleTreeDao;
    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;
    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //先从redis中查找strategyID的策略奖品实体
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        //如果在redis中查找的到，就返回策略奖品实体
        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty())
            return strategyAwardEntities;

        //如果在redis中查找不到，在库中读取数据
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards){
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }

        redisService.setValue(cacheKey,strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardRateSearchTable(String key, BigDecimal rateRange, Map<Integer, Integer> shuffleStrategyAwardRateSearchTable) {
        //1.存储抽奖策略范围值，如10000
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+key, rateRange.intValue());
        //2.存储概率查找表
        Map<Integer,Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+key);
        cacheRateTable.putAll(shuffleStrategyAwardRateSearchTable);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+key);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey) {
        return getStrategyAwardAssemble(String.valueOf(strategyId),rateKey);
    }

    @Override
    public Integer getStrategyAwardAssemble(String key, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+key,rateKey);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        //先从redis中查找strategyID的策略奖品实体
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        //如果在redis中查找的到，就返回策略实体
        if (null != strategyEntity )
            return strategyEntity;

        //如果在redis中查找不到，在库中读取数据
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);

        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {

        //strategy rule的内容不需要从redis缓存中查询，直接从mysql 获得dao数据持久化对象
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule strategyRule = strategyRuleDao.queryStrategyRule(strategyRuleReq);

        return StrategyRuleEntity.builder()
                .strategyId(strategyRule.getStrategyId())
                .awardId(strategyRule.getAwardId())
                .ruleType(strategyRule.getRuleType())
                .ruleModel(strategyRule.getRuleModel())
                .ruleValue(strategyRule.getRuleValue())
                .ruleDesc(strategyRule.getRuleDesc())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        strategyRule.setRuleModel(ruleModel);

        return strategyRuleDao.queryStrategyRuleValue(strategyRule);
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId,null,ruleModel);
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        String ruleModels = strategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
        if(ruleModels == null) return null;
        return StrategyAwardRuleModelVO.builder()
                .ruleModels(ruleModels)
                .build();
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        //1.先从缓存中查找, 不为空的话直接返回
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY+treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if(ruleTreeVOCache != null)
                return ruleTreeVOCache;

        //2.从数据库MySQL中获取
        //现根据树id获取ruleTree PO, ruleTreeNodes, ruleTreeNodeLines
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);

        //node line构造map{ruleNodeFrom:[对应的line列表]}给node用构造treeNodeMap 给tree用

        //2.1将连线放到map结构中
        // 1. tree node line 转换Map结构
        // Map:{ruleNodeFrom:[对应的line列表]}
        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for(RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines){
            //构建每一个line po对应的vo
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();


            //computeIfAbsent查看有没有ruleNodeFrom对应的列表，没有就新建一个，有的话之间返回
            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
        }
        log.info("新建的连线Map是:\n{}", JSON.toJSONString(ruleTreeNodeLineMap, true));


        //2.2 将节点转换成map结构
        //规则节点
        //key:ruleKey, value: RuleTreeNode
        //private Map<String, RuleTreeNodeVO> treeNodeMap;
        Map<String, RuleTreeNodeVO> treeNodeVOMap = new HashMap<>();
        for(RuleTreeNode ruleTreeNode : ruleTreeNodes){
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
                    .build();
            treeNodeVOMap.put(ruleTreeNode.getRuleKey(),ruleTreeNodeVO);
        }
        log.info("构建的节点Map是:\n{}", JSON.toJSONString(treeNodeVOMap, true));

        //2.3 构建rule tree
        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeRootRuleKey())
                .treeNodeMap(treeNodeVOMap)
                .build();

        redisService.setValue(cacheKey, ruleTreeVODB);
        return ruleTreeVODB;
    }

    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, awardCount);
    }

    /**
     * 缓存key，decr 方式扣减库存
     *
     * @param cacheKey 缓存Key
     * @return 扣减结果
     */
    @Override
    public Boolean subtractionAwardStock(String cacheKey) {
        //将目前对应的库存值进行原子操作减1，并返回decr之后的库存值给surplus
        long surplus = redisService.decr(cacheKey);
        if (surplus < 0){
            //库存被扣减到0一下了，发生错误了
            //恢复库存为0个
            redisService.setValue(cacheKey,0);
            return false;
        }
        //组装库存锁的key。 cacheKey_库存扣减后的值
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        Boolean lock = redisService.setNx(lockKey);
        //如果加锁失败
        if (!lock){
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    /**
     * 写入奖品库存消费队列
     *
     * @param strategyAwardStockKeyVO 对象值对象
     */
    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
        //构建缓存键
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
        //获取阻塞队列，用于在延迟时间到达后存储需要处理的任务
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        //获取延迟队列，strategyAwardStockKeyVO在等待后，转移到阻塞队列
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        //3秒将对象放入阻塞队列
        delayedQueue.offer(strategyAwardStockKeyVO, 3, TimeUnit.SECONDS);
    }
}
