package com.priska.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 活动账户对象
 * @author: Priska
 * @create: 2025-01-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 总数量
     */
    private Integer totalCount;
    /**
     * 总数量 剩余数量
     */
    private Integer totalCountSurplus;
    /**
     * 日数量
     */
    private Integer dayCount;
    /**
     * 日数量剩余数量
     */
    private Integer dayCountSurplus;
    /**
     * 月数量
     */
    private Integer monthCount;
    /**
     * 月数量剩余数量
     */
    private Integer monthCountSurplus;
}
