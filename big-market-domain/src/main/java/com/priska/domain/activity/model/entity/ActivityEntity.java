package com.priska.domain.activity.model.entity;

import com.priska.domain.activity.model.valobj.ActivityStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 活动实体对象
 * @author: Priska
 * @create: 2025-01-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEntity {
    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /*
     * 开始时间
     * */
    private Date beginDateTime;

    /*
     * 结束时间
     * */
    private Date endDateTime;

    /**
     * 抽奖策略id，对应strategy那张表的主键
     */
    private Long strategyId;

    private ActivityStateVO state;
}
