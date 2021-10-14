package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.controller.LoginController;
import com.yangbo.seckill.seckill.exception.GlobalException;
import com.yangbo.seckill.seckill.dao.SeckillUserDao;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.redis.SeckillUserKey;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.util.MD5Util;
import com.yangbo.seckill.seckill.util.UUIDUtil;
import com.yangbo.seckill.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class SeckillUserService {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SeckillUserDao seckillUserDao;
    @Autowired
    RedisService redisService;
    //定义cookie
    public static final String COOKIE_NAME_TOKEN = "token";

    //设置对象缓存  通过手机号查找用户
    public SeckillUser getByIdSer(long id){
        //取出缓存
        SeckillUser user = redisService.get(SeckillUserKey.getById,""+id,SeckillUser.class);
        if(user!=null){
            return user;
        }
        //取数据库
        user = seckillUserDao.getById(id);
        if(user!=null){
            redisService.set(SeckillUserKey.getById,""+id,user);
        }
        return user;
    }

    //更新数据库密码操作
    public Boolean updatePass(String token,long id,String passwordNew){
        //取user
        SeckillUser user = getByIdSer(id);
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXISTS);
        }
        //更改数据库
        SeckillUser toBeUpdate = new SeckillUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDbPass(passwordNew,user.getSalt()));
        seckillUserDao.update(toBeUpdate);
        //修改缓存
        redisService.delete(SeckillUserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SeckillUserKey.token,token,user);
        return true;
    }

    //对业务层代码进行优化
    // CodeMsg方法转化成 boolean方法   service只注重业务层代码
    public Boolean login(HttpServletResponse httpServletResponse,LoginVo loginVo){
        log.info("1"+loginVo.toString());
        if(loginVo==null){
            //return CodeMsg.SERVER_ERROR;
            //定义全局异常后，可以将异常直接外抛
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        log.info("2"+mobile+password);
        //查看数据库中是否含有此手机和密码
        // 并将密码进行比对
        SeckillUser user = getByIdSer(Long.parseLong(mobile));
        if(user == null){
            //return CodeMsg.MOBILE_NOT_EXISTS;
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXISTS);
        }

        //获取数据库中的salt加密值
        String salt = user.getSalt();
        String dbPass = user.getPassword();
        String calPass = MD5Util.formPassToDbPass(password,salt);
        //判断密码是否相等
        if(!calPass.equals(dbPass)){
            //return CodeMsg.PASSWORD_ERROR;
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }


//        //登录成功后，服务器端  生成cookie  String类型
//        String token = UUIDUtil.uuid();
//        //标识 哪一个用户对应的 信息,将用户和cookie信息绑定，redis缓存里面有用户信息
//        //将个人信息放到第三方放（redis）的缓存里面   ----前缀/序列号/数据库名字----
//        redisService.set(SeckillUserKey.token,token,user);
//        //生成cookie
//        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
//        //设置有效期为秒杀的有效期
//        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
//        cookie.setPath("/");
//        log.info("name: "+cookie.getName());
//        log.info("value: "+cookie.getValue());
//        //记录cookie
//        httpServletResponse.addCookie(cookie);
        String token =UUIDUtil.uuid();
        addCookie(httpServletResponse,token,user);
        return true;
        //return CodeMsg.SUCCESS;
    }

    //如果缓存里面有，通过redis缓存获取 value秒杀用户对象，比较
    public SeckillUser getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        //增加过期时间  通过token值，从缓存里面获取用户
        SeckillUser seckillUser =  redisService.get(SeckillUserKey.token,token,SeckillUser.class);
        //生成cookie,延长有效期
        if(seckillUser!=null){
            addCookie(response,token,seckillUser);
        }
        return seckillUser;
    }

    private void addCookie(HttpServletResponse response,String token,SeckillUser user){
        //登录成功后，服务器端  生成cookie  String类型
        //标识 哪一个用户对应的 信息,将用户和cookie信息绑定，redis缓存里面有用户信息
        //将个人信息放到第三方放（redis）的缓存里面   ----前缀/序列号/数据库名字----
        redisService.set(SeckillUserKey.token,token,user);
        //生成cookie
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        //设置有效期为秒杀的有效期
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        cookie.setPath("/");

        //记录cookie
        response.addCookie(cookie);
    }
}
