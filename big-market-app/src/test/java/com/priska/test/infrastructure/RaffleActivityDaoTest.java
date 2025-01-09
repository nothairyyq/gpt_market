package com.priska.test.infrastructure;

import com.alibaba.fastjson2.JSON;
import com.priska.infrastructure.persistent.dao.IRaffleActivityDao;
import com.priska.infrastructure.persistent.po.RaffleActivity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description:
 * @author: Priska
 * @create: 2025-01-09
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityDaoTest {
    @Resource
    IRaffleActivityDao raffleActivityDao;

    @Test
    public void test_raffleActivityDao(){

        RaffleActivity raffleActivity =  raffleActivityDao.queryRaffleActivityByActivityId(100301L);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivity));
    }
}
