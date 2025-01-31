package com.priska.domain.activity.model.aggregate;

import com.priska.domain.activity.model.entity.ActivityOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 下单聚合对象
 * @author: Priska
 * @create: 2025-01-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderAggregate {

    /** 用户ID */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 总次数
     */
    private Integer totalCount;
    /**
     * 日次数
     */
    private Integer dayCount;
    /**
     * 月次数
     */
    private Integer monthCount;
    /**
     * 活动订单实体
     */
    private ActivityOrderEntity activityOrderEntity;
}
