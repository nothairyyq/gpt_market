package com.priska.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动订单PO
 * @author: Priska
 * @create: 2024-12-20
 */
@Data
public class RaffleActivityOrder {
    private Long id;
    private String userId;
    private Long sku;
    private Long activityId;
    private String activityName;
    private Long strategyId;
    private String orderId;
    private Date orderTime;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private String state;
    private String outBusinessNo;
    private Date createTime;
    private Date updateTime;
}
