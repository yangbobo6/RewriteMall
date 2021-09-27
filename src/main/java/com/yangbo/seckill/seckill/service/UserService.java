package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.dao.UserDao;
import com.yangbo.seckill.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getServiceById(int id){
        return userDao.getById(id);
    }
}
