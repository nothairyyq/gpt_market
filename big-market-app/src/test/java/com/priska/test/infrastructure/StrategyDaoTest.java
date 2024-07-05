package com.priska.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.priska.infrastructure.persistent.dao.IStrategyDao;
import com.priska.infrastructure.persistent.po.Strategy;
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
public class StrategyDaoTest {
    @Resource
    private IStrategyDao strategyDao;
    @Test
    public void test_queryStrategyList(){
        List<Strategy> strategys = strategyDao.queryStrategyList();
        log.info("测试结果：{}", JSON.toJSONString(strategys));
    }
}
