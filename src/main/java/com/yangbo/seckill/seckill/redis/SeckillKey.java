package com.yangbo.seckill.seckill.redis;

public class SeckillKey extends BasePrefix {

    public SeckillKey(String prefix) {
        super(prefix);
    }

    public SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static final SeckillKey isGoodsOver = new SeckillKey("go");
    public static final SeckillKey getSeckillPath = new SeckillKey(60,"gp");
    public static final SeckillKey getVerifyCode = new SeckillKey(500,"vc");
}
