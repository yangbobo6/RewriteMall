package com.yangbo.seckill.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.List;
@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter {
//    @Autowired  //注入进来
//    UserArgumentResolver userArgumentResolver;
//
//    //方法的作用   在controller中 带的很多参数 response request model等，由这个方法赋值，
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//
//        argumentResolvers.add(userArgumentResolver);
//    }
//}
public class WebConfig implements WebMvcConfigurer{
    @Autowired
    UserArgumentResolver userArgumentResolver;

        //方法的作用   在controller中 带的很多参数 response request model等，由这个方法赋值，
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        argumentResolvers.add(userArgumentResolver);
    }

}