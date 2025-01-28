package com.priska.test.infrastructure.activity;

import com.alibaba.fastjson.JSON;
import com.priska.infrastructure.persistent.dao.IRaffleActivitySkuDao;
import com.priska.infrastructure.persistent.po.RaffleActivityOrder;
import com.priska.infrastructure.persistent.po.RaffleActivitySku;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: IntelliJ IDEA
 * @description: 活动sku测试
 * @author: Priska
 * @create: 2025-01-28
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivitySkuDaoTest {

    @Resource
    IRaffleActivitySkuDao raffleActivitySkuDao;

    @Test
    public void test_queryRaffleActivityOrderByUserId() {
        Long sku = 9011L;
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivitySku));
    }
}
