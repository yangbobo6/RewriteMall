package com.yangbo.seckill.seckill.Controller;

import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.domain.User;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.redis.SeckillUserKey;
import com.yangbo.seckill.seckill.service.SeckillUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    SeckillUserService seckillUserService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String toGoods(HttpServletResponse response,Model model,
           @CookieValue(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
           @RequestParam(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String paraToken
    ){
        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paraToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paraToken)?cookieToken:paraToken;
        SeckillUser user = seckillUserService.getByToken(response,token);
        model.addAttribute("user",user);
        //model.addAttribute("user",new SeckillUser());
        return "goodlist1";
    }
}
