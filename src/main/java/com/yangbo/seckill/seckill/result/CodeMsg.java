package com.yangbo.seckill.seckill.result;

import com.sun.tools.javac.jvm.Code;

public class CodeMsg {
    private int code;
    private String msg;

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(50100,"服务端异常");
    //订单异常

    //登录异常
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500210,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500211,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500212,"手机号码不符合标准");
    public static CodeMsg MOBILE_NOT_EXISTS = new CodeMsg(500213,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500214,"密码错误");

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
