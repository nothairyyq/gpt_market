package com.priska.test.infrastructure;


import com.alibaba.fastjson.JSON;
import com.priska.infrastructure.persistent.dao.IStrategyRuleDao;
import com.priska.infrastructure.persistent.po.StrategyRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRuleDaoTest {
    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Test
    public void test_queryStrategyRuleList(){
        List<StrategyRule> strategyRules = strategyRuleDao.queryStrategyRuleList();
        log.info("测试结果：{}", JSON.toJSONString(strategyRules) );
    }
}
