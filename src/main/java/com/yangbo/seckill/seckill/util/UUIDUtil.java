package com.yangbo.seckill.seckill.util;

import java.util.UUID;

//生成cookie的  序号
public class UUIDUtil {

    public static String uuid(){
        //原生的uuid带横杠，将其去掉
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void main(String[] args) {
        Object o = UUIDUtil.uuid();
        String s = UUIDUtil.uuid();
        String v = UUIDUtil.uuid();

        System.out.println(o);
        System.out.println(s);
        System.out.println(v);
    }
}
