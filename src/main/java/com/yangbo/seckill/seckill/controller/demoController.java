package com.yangbo.seckill.seckill.controller;

import com.yangbo.seckill.seckill.rabbitmq.MQSender;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class demoController {

    @Autowired
    MQSender mqSender;

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
//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> rabbitmq(){
//        mqSender.send("123");
//        return Result.success("hello");
//    }
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> rabbitmq2(){
//        mqSender.sendTopic("topicAndYangbobo");
//        return Result.success("topic Success");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> rabbitmq3(){
//        mqSender.sendFanout("fanout");
//        return Result.success("fanout Success");
//    }

//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> rabbitmq4(){
//        mqSender.sendHeader("header Message");
//        return Result.success("header Success");
//    }

}