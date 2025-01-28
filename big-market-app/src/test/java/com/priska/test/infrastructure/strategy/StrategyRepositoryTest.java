package com.priska.test.infrastructure.strategy;

import com.alibaba.fastjson.JSON;
import com.priska.domain.strategy.model.valobj.RuleTreeVO;
import com.priska.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description: 策略仓储测试
 * @author: Priska
 * @create: 2024-10-22
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRepositoryTest {
    @Resource
    private IStrategyRepository strategyRepository;

    @Test
    public void queryRuleTreeVOByTreeId(){
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId("tree_lock");
        log.info("最终测试结果，ruleTreeVO:\n{}", JSON.toJSONString(ruleTreeVO, true));

    }
}
