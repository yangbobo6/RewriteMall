package com.yangbo.seckill.seckill.controller;


import com.yangbo.seckill.seckill.domain.OrderInfo;
import com.yangbo.seckill.seckill.domain.SeckillOrder;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.rabbitmq.MQSender;
import com.yangbo.seckill.seckill.rabbitmq.SeckillMessage;
import com.yangbo.seckill.seckill.redis.GoodsKey;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import com.yangbo.seckill.seckill.service.GoodsService;
import com.yangbo.seckill.seckill.service.OrderService;
import com.yangbo.seckill.seckill.service.SeckillService;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class SeckillController implements InitializingBean {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;
    private Map<Long,Boolean> localOverMap = new HashMap<Long,Boolean>();


    /**
     * 回调这个方法  系统初始化时候,将库存量放入redis里面
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null){
            return;
        }
        for (GoodsVo goods:goodsList){
            redisService.set(GoodsKey.getSeckillGoodStock,""+goods.getId(),goods.getStockCount());
            //商品id做标记，没有结束
            localOverMap.put(goods.getId(),false);
        }
    }



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
            return "miaosha_fail";
        }
        //判断是否已经秒杀成功  (who,what)  去秒杀订单里面查询是否有
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if(order != null){
            model.addAttribute("errmsg", CodeMsg.REPEAT_SECKILL.getMsg());
            return "miaosha_fail";
        }

        //秒杀过程   下订单  减库存  写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(seckillUser,goods);
        model.addAttribute("orderInfo",orderInfo); //显示订单信息
        model.addAttribute("goods",goods);
        return "order_detail";
    }

    //ajax静态页面缓存    前后端分离
    @RequestMapping(value = "/do_miaosha1",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list1(Model model, SeckillUser seckillUser,
                        @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",seckillUser);
        if(seckillUser==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //6.2 用法  内存标记，减少redis的访问
        Boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //6.1 用法   预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodStock,""+goodsId);
        if(stock < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀成功  (who,what)  去秒杀订单里面查询是否有
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }
        //6.3 rabbitmq 缓存操作   入队操作
        SeckillMessage message = new SeckillMessage();
        message.setUser(seckillUser);
        message.setGoodsId(goodsId);
        sender.seckillMessage(message);

        return Result.success(0);

        /**
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀成功  (who,what)  去秒杀订单里面查询是否有
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }

        //秒杀过程   下订单  减库存  写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(seckillUser,goods);

        return Result.success(orderInfo);
         **/

    }
    //成功的时候轮询  秒杀成功，返回订单id，失败则返回-1  0 派对中  成功后转入支付页面
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, SeckillUser seckillUser,
                                 @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", seckillUser);
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //查询是否存在订单，如果有则 转入支付页面
        long result = seckillService.getSeckillResult(seckillUser.getId(),goodsId);
        return Result.success(result);
    }

    //请求接口获取秒杀地址
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> seckillResul(Model model, SeckillUser seckillUser,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", seckillUser);
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }


        return Result.success("");
    }



}
