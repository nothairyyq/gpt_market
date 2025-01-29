package com.priska.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: IntelliJ IDEA
 * @description: 抽奖活动sku实体
 * @author: Priska
 * @create: 2025-01-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityShopCartEntity {
    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
}
