package com.yangbo.seckill.seckill.access;

import com.yangbo.seckill.seckill.domain.SeckillUser;


//多线程的时候，是线程安全的一种访问方式，和当前线程绑定
public class UserContext {
    //本地线程  
    private static ThreadLocal<SeckillUser> userThreadLocal = new ThreadLocal<SeckillUser>();
    //
    public static void setUser(SeckillUser user){
        userThreadLocal.set(user);
    }

    public static SeckillUser getUser(){
        return userThreadLocal.get();
    }
}
