package com.yangbo.seckill.seckill.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yangbo
 * @Date: 2022-03-10-10:28
 * @Description:  AOP的一个案例  理解切面等
 */
@RestController
public class AOPHelloController {
    @GetMapping("/getHello")
    public String getHello(){
        System.out.println("执行Hello");
        return "hello";
    }

}
