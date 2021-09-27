package com.yangbo.seckill.seckill.Controller;

import com.yangbo.seckill.seckill.domain.User;
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
}
