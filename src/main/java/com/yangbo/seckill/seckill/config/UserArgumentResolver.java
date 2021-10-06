package com.yangbo.seckill.seckill.config;

import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    SeckillUserService userService;

    //检查是否是seckilluser的 对象类型，是此类型才参数处理
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz== SeckillUser.class;
    }

    //处理参数
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
           NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //获取请求对象和响应对象
        HttpServletRequest request =webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
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
