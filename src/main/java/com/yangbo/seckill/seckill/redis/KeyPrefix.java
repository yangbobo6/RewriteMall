package com.yangbo.seckill.seckill.redis;


//设置前缀的接口  表示该公共的方法
public interface KeyPrefix {
    //获取过期时间
    public int expireSeconds();
    //设置前缀
    public String getPrefix();
}
