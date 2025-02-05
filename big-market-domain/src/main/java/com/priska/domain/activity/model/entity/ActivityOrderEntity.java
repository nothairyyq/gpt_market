package com.priska.domain.activity.model.entity;

import com.priska.domain.activity.model.valobj.ActivityStateVO;
import com.priska.domain.activity.model.valobj.OrderStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: IntelliJ IDEA
 * @description: 活动订单实体对象
 * @author: Priska
 * @create: 2025-01-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityOrderEntity {
    /**
     * 用户ID
     */
    private String userId;
    private Long sku;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 抽奖策略id，对应strategy那张表的主键
     */
    private Long strategyId;

    /**
     * 活动订单id
     */
    private String orderId;

    /**
     * 活动下单时间
     */
    private Date orderTime;

    /**
     * 总数量
     */
    private Integer totalCount;

    /**
     * 日数量
     */
    private Integer dayCount;

    /**
     * 月数量
     */
    private Integer monthCount;

    private OrderStateVO state;

    /**
     * 业务仿重ID - 外部透传的，确保幂等
     */
    private String outBusinessNo;

}
