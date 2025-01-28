package com.priska.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 活动sku表，解耦RaffleActivity和ActivityOrder
 * @author: Priska
 * @create: 2025-01-28
 */
@Data
public class RaffleActivitySku {
    private Long id;
    private Long sku;
    private Long activityId;
    private Long activityCountId;
    private Integer stockCount;
    private Integer stockCountSurplus;
    private Date createTime;
    private Date updateTime;
}
