package com.yangbo.seckill.seckill.access;

import com.alibaba.fastjson.JSON;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.redis.AccessKey;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import com.yangbo.seckill.seckill.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Service
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    SeckillUserService userService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            //获取用户，保存起来
            SeckillUser users = getUsers(request, response);
            //存到本地线程  threadLocal
            UserContext.setUser(users);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();

            if(needLogin){
                if(users==null){
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key+="_"+users.getId();
            }else {
                //do Nothings

            }

            //martine flower 重构-改善既有代码的设计
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            if(count==null){
                redisService.set(ak,key,1);
            }else if(count<maxCount){
                redisService.incr(ak,key);
            }else {
                render(response,CodeMsg.ACCESS_COUNT_REACH);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg cm) throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private SeckillUser getUsers(HttpServletRequest request,HttpServletResponse response){
        //获取请求中token的参数
        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
        //获取  cookie中  token参数
        String cookieToken = getCookieValue(request,SeckillUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)) {
            return null;
        }
        //优先获取paramToken
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        //通过token获取 Seckilluser 对象
        return userService.getByToken(response,token);
    }
    
    //遍历request请求中的cookie数组，看里面有没有token对象
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies  = request.getCookies();
        if(cookies == null || cookies.length <= 0) {
            return null;
        }
        for(Cookie cookie:cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
