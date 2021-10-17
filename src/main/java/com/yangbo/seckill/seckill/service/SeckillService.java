package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.dao.GoodsDao;
import com.yangbo.seckill.seckill.domain.Goods;
import com.yangbo.seckill.seckill.domain.OrderInfo;
import com.yangbo.seckill.seckill.domain.SeckillOrder;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.redis.SeckillKey;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    //通常来讲，自己的service里面只引入自己的dao，或者引入别人的service ********
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;

    @Transactional  //原子操作
    public OrderInfo seckill(SeckillUser seckillUser, GoodsVo goods) {
        //减少库存  下订单  写入秒杀订单
        //goods 是秒杀的产品
        boolean success = goodsService.reduceStock(goods);
        if (success){
            //减库存成功   生成订单
            return orderService.createOrder(seckillUser,goods);
        }else {
            //库存没减少，秒杀未成功
            setGoodsOver(goods.getId());
            return null;
        }

    }

    public long getSeckillResult(Long id, long goodsId) {
        //获取秒杀订单
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(id,goodsId);
        if(order!=null){
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){ //秒杀失败
                return -1;
            }else { //在排队中
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver,""+goodsId);
    }
}
