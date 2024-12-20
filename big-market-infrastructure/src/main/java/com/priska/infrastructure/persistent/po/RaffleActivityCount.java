package com.priska.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖次数配置表 持久化对象
 * @author: Priska
 * @create: 2024-12-20
 */
@Data
public class RaffleActivityCount {
    private Long id;
    private Long activityCountId;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private Date createTime;
    private Date updateTime;
}
