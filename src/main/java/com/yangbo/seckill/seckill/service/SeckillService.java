package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.dao.GoodsDao;
import com.yangbo.seckill.seckill.domain.Goods;
import com.yangbo.seckill.seckill.domain.OrderInfo;
import com.yangbo.seckill.seckill.domain.SeckillUser;
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

    @Transactional  //原子操作
    public OrderInfo seckill(SeckillUser seckillUser, GoodsVo goods) {
        //减少库存  下订单  写入秒杀订单
        //goods 是秒杀的产品
        goodsService.reduceStock(goods);
        //生成订单
        return orderService.createOrder(seckillUser,goods);

    }
}
