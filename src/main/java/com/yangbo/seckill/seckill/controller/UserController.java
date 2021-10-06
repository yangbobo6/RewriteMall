package com.yangbo.seckill.seckill.controller;

import com.yangbo.seckill.seckill.domain.User;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.redis.UserKey;
import com.yangbo.seckill.seckill.result.Result;
import com.yangbo.seckill.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getById")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getServiceById(1);
        System.out.println(user.getName());
        return Result.success(user);
    }

    //测试事务
    @RequestMapping("/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @Autowired
    RedisService redisService;

    @RequestMapping("/redis")
    @ResponseBody
//    public Result<String> redisGet(){
//        String v1 = redisService.get(UserKey.getById,"key1",String.class);
//        return Result.success(v1);
//    }
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user);
    }


    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById,""+1,user); //UserKey:id1
        return Result.success(true);
    }




}
