package com.yangbo.seckill.seckill.controller;

import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.redis.GoodsKey;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.result.Result;
import com.yangbo.seckill.seckill.service.GoodsService;
import com.yangbo.seckill.seckill.service.SeckillUserService;
import com.yangbo.seckill.seckill.vo.GoodsDetailVo;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger log = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    SeckillUserService seckillUserService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toGoods(HttpServletRequest request,HttpServletResponse response, Model model, SeckillUser user
//           @CookieValue(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
//           @RequestParam(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String paraToken
    ){
        //前端通过model可以取到值了
        model.addAttribute("user",user);
        //查询商品列表
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsVos);


        //return "goods_list";
        //第五课时   将页面缓存到redis里面  取出缓存
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        log.info(html);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染
        IWebContext ctx = new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());
        //手动渲染

        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }


    @RequestMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody               //请求路径中占位符的值
    public String detail(HttpServletRequest request,HttpServletResponse response,
                         Model model, SeckillUser seckillUser,
                         @PathVariable("goodsId") long goodsId){
        model.addAttribute("user",seckillUser);

        //取缓存
        String html =redisService.get(GoodsKey.getGoodsList,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

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

        //return "goods_detail";
        IWebContext ctx = new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

    //静态页面  纯html  jQuery页面
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody               //请求路径中占位符的值
    public Result<GoodsDetailVo> detail2(HttpServletRequest request, HttpServletResponse response,
                                         Model model, SeckillUser seckillUser,
                                         @PathVariable("goodsId") long goodsId){
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

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
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(seckillUser);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainTime);

        return Result.success(vo);
    }

}
