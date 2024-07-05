package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
//抽奖策略Dao
public interface IStrategyDao {
    List<Strategy> queryStrategyList();
}
