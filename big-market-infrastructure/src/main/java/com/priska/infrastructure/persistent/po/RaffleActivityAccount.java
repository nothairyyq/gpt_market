package com.priska.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动账户表 po
 * @author: Priska
 * @create: 2024-12-20
 */
@Data
public class RaffleActivityAccount {
    private Long id;
    private String userId;
    private Long activityId;
    private Integer totalCount;
    private Integer totalCountSurplus;
    private Integer dayCount;
    private Integer dayCountSurplus;
    private Integer monthCount;
    private Integer monthCountSurplus;
    private Date createTime;
    private Date updateTime;
}
