package com.priska.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.priska.infrastructure.persistent.dao.IStrategyAwardDao;
import com.priska.infrastructure.persistent.po.StrategyAward;
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
public class StrategyAwardDaoTest {

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Test
    public void test_queryStrategyAwardList(){
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
        log.info("测试结果：{}", JSON.toJSONString(strategyAwards));
    }


}
