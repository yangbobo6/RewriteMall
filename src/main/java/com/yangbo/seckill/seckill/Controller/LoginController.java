package com.yangbo.seckill.seckill.Controller;

import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import com.yangbo.seckill.seckill.service.SeckillUserService;
import com.yangbo.seckill.seckill.service.UserService;
import com.yangbo.seckill.seckill.util.ValidatorUtil;
import com.yangbo.seckill.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    SeckillUserService seckillUserService;

    //输出日志
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    //跳转到登陆页面
    @RequestMapping("to_login")
    public String toLogin(){
        return "login";
    }

    //登录信息的比较
    @RequestMapping("/do_login")
    public Result<Boolean> doLogin(LoginVo loginVo){
        logger.info(loginVo.toString());
        //获取到前端传输的值
        String mobile = loginVo.getMobile();
        String passInput = loginVo.getPassword();
        //参数校验
        if(StringUtils.isEmpty(passInput)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        //是否为空
        if(StringUtils.isEmpty(mobile)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        //辨别是否为手机号码
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        //登录  调用service层
        CodeMsg cm = seckillUserService.login(loginVo);
        if (cm.getCode()==0){
            return Result.success(true);
        }else {
            return Result.error(cm);
        }
    }

}
