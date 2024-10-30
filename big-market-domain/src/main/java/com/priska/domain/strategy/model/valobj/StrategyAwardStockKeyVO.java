package com.priska.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 策略奖品库存key标识值对象
 * @author: Priska
 * @create: 2024-10-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyAwardStockKeyVO {
    private Long strategyId;
    private Integer awardId;
}
