package com.yangbo.seckill.seckill.result;

public class CodeMsg {
    private int code;
    private String msg;

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //通用异常   直接创建CodeMsg对象
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(50100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常： %s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500502,"请求非法");
    public static CodeMsg ACCESS_COUNT_REACH = new CodeMsg(500503,"请求到达上限");

    //订单异常
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500300,"订单不能为空");
    //登录异常
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500210,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500211,"手机号不能为空");
    public static CodeMsg SESSION_ERROR = new CodeMsg(500215,"session不存在或已失效");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500212,"手机号码不符合标准");
    public static CodeMsg MOBILE_NOT_EXISTS = new CodeMsg(500213,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500214,"密码错误");

    //秒杀模块
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500,"商品已秒杀完");
    public static CodeMsg REPEAT_SECKILL = new CodeMsg(500501,"不能重复秒杀");
    public static CodeMsg Seckill_FAILED = new CodeMsg(500502,"秒杀失败");

    //
    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

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
