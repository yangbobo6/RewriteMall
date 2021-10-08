package com.yangbo.seckill.seckill.controller;

import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.service.GoodsService;
import com.yangbo.seckill.seckill.service.SeckillUserService;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    SeckillUserService seckillUserService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toGoods(HttpServletResponse response,Model model,SeckillUser user
//           @CookieValue(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
//           @RequestParam(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String paraToken
    ){
        //前端通过model可以取到值了
        model.addAttribute("user",user);
        //查询商品列表
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsVos);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
//    @ResponseBody               //请求路径中占位符的值
    public String detail(Model model, SeckillUser seckillUser,
                         @PathVariable("goodsId") long goodsId){
        model.addAttribute("user",seckillUser);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainTime = 0;
        if(now<startAt){ //秒杀未开始
            miaoshaStatus = 0;
            remainTime =(int)((startAt-now)/1000);
        }else if(now>endAt){  //秒杀已经结束
            miaoshaStatus = 2;
            remainTime = -1;
        }else {   //秒杀正在进行中
            miaoshaStatus = 1;
            remainTime = 0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainTime);

        return "goods_detail";
    }



}
