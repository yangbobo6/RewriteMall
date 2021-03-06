package com.yangbo.seckill.seckill.controller;

import com.yangbo.seckill.seckill.access.AccessLimit;
import com.yangbo.seckill.seckill.domain.OrderInfo;
import com.yangbo.seckill.seckill.domain.SeckillOrder;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.rabbitmq.MQSender;
import com.yangbo.seckill.seckill.rabbitmq.SeckillMessage;
import com.yangbo.seckill.seckill.redis.AccessKey;
import com.yangbo.seckill.seckill.redis.GoodsKey;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.redis.SeckillKey;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import com.yangbo.seckill.seckill.service.GoodsService;
import com.yangbo.seckill.seckill.service.OrderService;
import com.yangbo.seckill.seckill.service.SeckillService;
import com.yangbo.seckill.seckill.util.MD5Util;
import com.yangbo.seckill.seckill.util.UUIDUtil;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.apache.coyote.Response;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
     * ??????????????????  ?????????????????????,??????????????????redis??????
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null){
            return;
        }
        for (GoodsVo goods:goodsList){
            redisService.set(GoodsKey.getSeckillGoodStock,""+goods.getId(),goods.getStockCount());
            //??????id????????????????????????
            localOverMap.put(goods.getId(),false);
        }
    }



    //????????????redis 
    @RequestMapping("/do_miaosha1")
    public String list(Model model, SeckillUser seckillUser,
                       @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",seckillUser);
        if(seckillUser==null){
            return "login";
        }
        //????????????
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return "miaosha_fail";
        }
        //??????????????????????????????  (who,what)  ????????????????????????????????????
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if(order != null){
            model.addAttribute("errmsg", CodeMsg.REPEAT_SECKILL.getMsg());
            return "miaosha_fail";
        }

        //????????????   ?????????  ?????????  ??????????????????
        OrderInfo orderInfo = seckillService.seckill(seckillUser,goods);
        model.addAttribute("orderInfo",orderInfo); //??????????????????
        model.addAttribute("goods",goods);
        return "order_detail";
    }

    //ajax??????????????????    ???????????????  ?????????????????????
    @RequestMapping(value = "/{path}/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list1(Model model, SeckillUser seckillUser,
                                 @RequestParam("goodsId")long goodsId,
                                 @PathVariable("path")String path){
        model.addAttribute("user",seckillUser);
        if(seckillUser==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //??????path
        boolean check = seckillService.checkPath(seckillUser,goodsId,path);
        if(!check){
            Result.error(CodeMsg.REQUEST_ILLEGAL);
        }


        //6.2 ??????  ?????????????????????redis?????????
        Boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //6.1 ??????   ????????????
        long stock = redisService.decr(GoodsKey.getSeckillGoodStock,""+goodsId);
        if(stock < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //??????????????????????????????  (who,what)  ????????????????????????????????????
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }
        //6.3 rabbitmq ????????????   ????????????
        SeckillMessage message = new SeckillMessage();
        message.setUser(seckillUser);
        message.setGoodsId(goodsId);
        sender.seckillMessage(message);

        return Result.success(0);

        /**
        //????????????
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //??????????????????????????????  (who,what)  ????????????????????????????????????
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }

        //????????????   ?????????  ?????????  ??????????????????
        OrderInfo orderInfo = seckillService.seckill(seckillUser,goods);

        return Result.success(orderInfo);
         **/
    }

    //?????????????????????  ???????????????????????????id??????????????????-1  0 ?????????  ???????????????????????????
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, SeckillUser seckillUser,
                                 @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", seckillUser);
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //??????????????????????????????????????? ??????????????????
        long result = seckillService.getSeckillResult(seckillUser.getId(),goodsId);
        return Result.success(result);
    }

    //??????????????????????????????
    @AccessLimit(seconds=5,maxCount=5,needLogin=true)  //???????????????????????????
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillPath(HttpServletRequest request,SeckillUser seckillUser,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam("verifyCode")int verifyCode) {
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //?????????????????????  ??????redis????????????
        String url = request.getRequestURI();
        String key = url + "_" + seckillUser.getId();
        Integer count = redisService.get(AccessKey.access, key, Integer.class);
        if(count==null){
            redisService.set(AccessKey.access,key,1);
        }else if(count<5){
            redisService.incr(AccessKey.access,key);
        }else {
            return Result.error(CodeMsg.ACCESS_COUNT_REACH);
        }


        //???????????????????????????????????? ????????? ??????
        boolean check = seckillService.checkVerifyCode(seckillUser,goodsId,verifyCode);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        String path = seckillService.createSeckillPath(seckillUser,goodsId);
        return Result.success(path);
    }

    //???????????????????????????
    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> verifyCode(HttpServletResponse response, SeckillUser seckillUser,
                                      @RequestParam("goodsId")long goodsId) {
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        BufferedImage image = seckillService.createVerifyCode(seckillUser,goodsId);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"JPEG",outputStream);
            outputStream.flush();
            outputStream.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Result.error(CodeMsg.Seckill_FAILED);
        }
        return null;
    }

}
