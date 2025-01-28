package com.priska.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 活动总表po
 * @author: Priska
 * @create: 2024-12-20
 */
@Data
public class RaffleActivity {
    private Long id;
    private Long activityId;
    private String activityName;
    private String activityDesc;
    private Date beginDateTime;
    private Date endDateTime;
    private Long strategyId;
    private String state;
    private Date createTime;
    private Date updateTime;
}
