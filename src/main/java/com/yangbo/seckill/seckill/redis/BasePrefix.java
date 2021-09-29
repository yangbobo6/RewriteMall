package com.yangbo.seckill.seckill.redis;

//设置前缀的抽象方法
public abstract class BasePrefix implements KeyPrefix{
    private int expireSeconds;
    private String prefix;

    //构造方法  0代表永不过期
    public BasePrefix(String prefix){
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    //设置过期时间
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }
    //设置key的前缀
    @Override
    public String getPrefix() {
        //Class类，是获取类的类模板实例对象，通过反射的机制获取。 *******
        String className = getClass().getSimpleName();
        return className+':'+prefix;
    }
}
