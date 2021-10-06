package com.yangbo.seckill.seckill.controller;

import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class demoController {

    @RequestMapping("/thymeleaf")
    public String hello(Model model){
        model.addAttribute("name","yangbo");
        //返回到template下面
        return "hello";
    }

    @RequestMapping("/helloSuccess")
    @ResponseBody
    public Result<String> helloSuccess(){
        return Result.success("hello,imooc");
        //return new Result<>(0,"successful","hello,mooc");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError(){
        return Result.error(CodeMsg.SERVER_ERROR);
        //return new Result<>(50010,"xxx");
        //return new Result<>(50020,"session失效");
    }
}
