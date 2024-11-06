package com.priska.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖查询列表返回数据dto
 * @author: Priska
 * @create: 2024-11-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaffleAwardListResponseDTO {
    //奖品id
    private Integer awardId;
    //奖品标题
    private String awardTitle;
    //奖品副标题[抽奖x后解锁]
    private String awardSubtitle;
    //奖品排序编号
    private Integer sort;
}
