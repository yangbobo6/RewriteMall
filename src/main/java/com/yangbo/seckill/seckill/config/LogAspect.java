package com.yangbo.seckill.seckill.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @Author: yangbo
 * @Date: 2022-03-10-10:35
 * @Description: 创建一个切面
 */
@Aspect
@Component
public class LogAspect {
    /**
     * 前置通知
     * 在方法执行之前执行
     */
    @Before("execution(* com.yangbo.seckill.seckill.controller.AOPHelloController.getHello(..))")
    public void before(){
        System.out.println("before.....");
    }

}
