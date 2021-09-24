package com.yangbo.seckill.seckill.result;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(T data) {
    }
    private Result(CodeMsg cmg){
        if(cmg==null){
            return;
        }
        this.code = cmg.getCode();
        this.msg = cmg.getMsg();
    }

    //成功的时候调用
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    //失败的时候调用
    public static <T> Result<T> error(CodeMsg cmg){
        return new Result<T>(cmg);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
