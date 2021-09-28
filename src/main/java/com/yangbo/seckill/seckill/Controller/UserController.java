package com.yangbo.seckill.seckill.Controller;

import com.yangbo.seckill.seckill.domain.User;
import com.yangbo.seckill.seckill.redis.RedisServer;
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
    RedisServer redisServer;

    @RequestMapping("/redis")
    @ResponseBody
    public Result<String> redisGet(){
        String v1 = redisServer.get("key1",String.class);
        return Result.success(v1);
    }


}
