package com.priska.infrastructure.persistent.dao;

import com.priska.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//奖品表DAO
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
