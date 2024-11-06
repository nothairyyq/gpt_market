package com.priska.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖请求参数
 * @author: Priska
 * @create: 2024-11-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaffleRequestDTO {
    private Long strategyId;
}
