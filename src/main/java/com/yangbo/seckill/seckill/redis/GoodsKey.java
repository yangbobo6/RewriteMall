package com.yangbo.seckill.seckill.redis;

public class GoodsKey extends BasePrefix{

    public GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    //商品的redis名称和过期时间
    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
    public static GoodsKey getSeckillGoodStock = new GoodsKey(0,"gs");
}
