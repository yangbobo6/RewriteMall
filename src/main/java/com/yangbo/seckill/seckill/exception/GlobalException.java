package com.yangbo.seckill.seckill.exception;

import com.yangbo.seckill.seckill.result.CodeMsg;

public class GlobalException extends RuntimeException{
    public static final long serialVersionUID =1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
