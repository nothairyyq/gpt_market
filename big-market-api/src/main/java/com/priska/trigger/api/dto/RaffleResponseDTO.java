package com.priska.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 随机抽奖响应DTO
 * @author: Priska
 * @create: 2024-11-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaffleResponseDTO {
    //奖品id
    private Integer awardId;
    //奖品排序编号
    private Integer awardIndex;
}
