package com.yangbo.seckill.seckill.dao;

import com.yangbo.seckill.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SeckillUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public SeckillUser getById(@Param("id")long id);


}
