package com.yangbo.seckill.seckill.dao;

import com.yangbo.seckill.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeckillUserDao {

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(SeckillUser toBeUpdate);

    @Select("select * from miaosha_user where id = #{id}")
    public SeckillUser getById(@Param("id")long id);
}
