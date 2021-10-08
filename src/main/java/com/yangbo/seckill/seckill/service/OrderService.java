package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.dao.OrderDao;
import com.yangbo.seckill.seckill.domain.OrderInfo;
import com.yangbo.seckill.seckill.domain.SeckillOrder;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;
    public SeckillOrder getSeckillOrderByUserIdGoodsId(Long id, long goodsId) {
        SeckillOrder seckillOrderByUserId = orderDao.getSeckillOrderByUserId(id, goodsId);
        return seckillOrderByUserId;
    }

    @Transactional
    public OrderInfo createOrder(SeckillUser seckillUser, GoodsVo goods) {
        //创建新的订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(seckillUser.getId());
        long orderId = orderDao.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(seckillUser.getId());
        orderDao.insertSeckillOrder(seckillOrder);
        return orderInfo;
    }
}
