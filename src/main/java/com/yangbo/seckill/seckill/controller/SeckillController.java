package com.yangbo.seckill.seckill.controller;


import com.yangbo.seckill.seckill.domain.OrderInfo;
import com.yangbo.seckill.seckill.domain.SeckillOrder;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.service.GoodsService;
import com.yangbo.seckill.seckill.service.OrderService;
import com.yangbo.seckill.seckill.service.SeckillService;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SeckillController {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping("/do_miaosha")
    public String list(Model model, SeckillUser seckillUser,
                       @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",seckillUser);
        if(seckillUser==null){
            return "login";
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return "miaosha_failed";
        }
        //判断是否已经秒杀成功  (who,what)  去秒杀订单里面查询是否有
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if(order != null){
            model.addAttribute("errmsg", CodeMsg.REPEAT_SECKILL.getMsg());
            return "miaosha_failed";
        }

        //秒杀过程   下订单  减库存  写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(seckillUser,goods);
        model.addAttribute("orderInfo",orderInfo); //显示订单信息
        model.addAttribute("goods",goods);
        return "order_detail";

    }

}