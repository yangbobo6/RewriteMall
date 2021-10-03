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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    //登录信息的比较   ？？ 怎样取到前端的mobile和password
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse httpServletResponse, @Valid LoginVo loginVo){
        //进行了Validation参数校验，由vo层接管


//        logger.info(loginVo.toString());
//        //获取到前端传输的值
//        String mobile = loginVo.getMobile();
//        String passInput = loginVo.getPassword();
//        //参数校验
//        if(StringUtils.isEmpty(passInput)){
//            //
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        //是否为空
//        if(StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        //辨别是否为手机号码
//        if(!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }


        //登录  直接调用service层
        seckillUserService.login(httpServletResponse,loginVo);
        return Result.success(true);

//        CodeMsg cm = seckillUserService.login(loginVo);

//        暂时不用此来处理异常情况，异常在globalException统一处理，进行优化controller更为清晰
//        if (cm.getCode()==0){
//            return Result.success(true);
//        }else {
//            return Result.error(cm);
//        }
    }

}
