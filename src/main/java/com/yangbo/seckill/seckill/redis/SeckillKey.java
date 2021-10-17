package com.yangbo.seckill.seckill.redis;

public class SeckillKey extends BasePrefix {

    public SeckillKey(String prefix) {
        super(prefix);
    }

    public static final SeckillKey isGoodsOver = new SeckillKey("go");
}
