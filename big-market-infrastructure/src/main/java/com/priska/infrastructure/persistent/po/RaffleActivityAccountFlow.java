package com.priska.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 活动订单流水PO
 * @author: Priska
 * @create: 2024-12-20
 */
@Data
public class RaffleActivityAccountFlow {
    private Integer id;
    private String userId;
    private Long activityId;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private String flowId;
    private String flowChannel;
    private String bizId;
    private Date createTime;
    private Date updateTime;
}
