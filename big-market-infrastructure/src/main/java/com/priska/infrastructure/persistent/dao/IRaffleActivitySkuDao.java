package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: IntelliJ IDEA
 * @description:
 * @param:
 * @return:
 * @author: Priska
 * @create: 2025-01-28
 */
@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(Long sku);
}
