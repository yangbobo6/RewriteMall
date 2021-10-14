package com.yangbo.seckill.seckill.redis;

//秒杀用户列表的值
public class SeckillUserKey extends BasePrefix{

    //设置有效期
    public static final int TOKEN_EXPIRE = 3600*24*2;
    public SeckillUserKey(String prefix) {
        super(prefix);
    }

    public SeckillUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //前缀  是tk
    public static SeckillUserKey token = new SeckillUserKey(TOKEN_EXPIRE,"tk");

    public static SeckillUserKey getById = new SeckillUserKey(0,"id");

}
