package com.yangbo.seckill.seckill.Exception;

import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.result.Result;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import java.util.List;

@ControllerAdvice
@ResponseBody
//异常捕获
public class GlobalExceptionHandler {
    //value  拦截所有的异常   可以定义拦截特定的异常
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        //如果是全局异常
        if(e instanceof GlobalException){
            GlobalException ex =(GlobalException)e;
            return Result.error(ex.getCodeMsg());
        }
        //如果是绑定异常*****
        if(e instanceof BindException){
            BindException ex = (BindException)e;
            List<ObjectError> allErrors = ex.getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String message = objectError.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(message));
        }else {
            //返回服务异常
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
