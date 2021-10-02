package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.Exception.GlobalException;
import com.yangbo.seckill.seckill.dao.SeckillUserDao;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.domain.User;
import com.yangbo.seckill.seckill.result.CodeMsg;
import com.yangbo.seckill.seckill.util.MD5Util;
import com.yangbo.seckill.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillUserService {
    @Autowired
    SeckillUserDao seckillUserDao;
    public SeckillUser getByIdSer(long id){
        return seckillUserDao.getById(id);
    }

    //对业务层代码进行优化
    // CodeMsg方法转化成 boolean方法   service只注重业务层代码
    public Boolean login(LoginVo loginVo){
        if(loginVo==null){
            //return CodeMsg.SERVER_ERROR;
            //定义全局异常后，可以将异常直接外抛
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
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
        if(!calPass.equals(dbPass)){
            //return CodeMsg.PASSWORD_ERROR;
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        return true;
        //return CodeMsg.SUCCESS;
    }

}
