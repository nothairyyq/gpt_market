package com.priska.trigger.job;

import com.priska.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.priska.domain.strategy.service.IRaffleStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: IntelliJ IDEA
 * @description: 将奖品库存从缓存更新到数据库。奖品库存消耗时不直接更新到数据库，是用redis更新缓存库存吗，异步队列更新数据库达到一致
 * @author: Priska
 * @create: 2024-10-29
 */

@Slf4j
@Component()
public class UpdateAwardStockJob {
    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){
        try {
            log.info("定时任务，更新奖品消耗库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            StrategyAwardStockKeyVO strategyAwardStockKeyVO = raffleStock.takeQueueValue();
            if (strategyAwardStockKeyVO == null)
                return;
            log.info("定时任务，更新奖品消耗库存 strategyId:{} awardId:{}", strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
            raffleStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
        } catch (Exception e){
            log.error("定时任务，更新奖品消耗库存失败", e);
        }
    }
}
