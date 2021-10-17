package com.yangbo.seckill.seckill.rabbitmq;

import com.yangbo.seckill.seckill.domain.SeckillOrder;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import com.yangbo.seckill.seckill.service.GoodsService;
import com.yangbo.seckill.seckill.service.OrderService;
import com.yangbo.seckill.seckill.service.SeckillService;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    public static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiver(String message){
        log.info("direct模式 receive message："+message);
        SeckillMessage mm = RedisService.stringToBean(message,SeckillMessage.class);
        SeckillUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        //获取商品数量
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            return;
        }
        //判断是否已经秒杀成功  (who,what)  去秒杀订单里面查询是否有
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null){
            return;
        }
        //减库存  写入秒杀订单
        seckillService.seckill(user,goods);

    }



//    /**
//     * direct模式 消息接收者
//     * @param message
//     */
//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receiver(String message){
//        log.info("direct模式 receive message："+message);
//    }
//
//    /**
//     * topic模式  消息接收者
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void topicReceiver1(String message){
//        log.info("topic模式1 receive message："+message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void topicReceiver2(String message){
//        log.info("topic模式2 receive message："+message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void headerReceiver(byte[] message){
//        log.info("header模式 receive message："+new String(message));
//    }

}
