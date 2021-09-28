package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.dao.UserDao;
import com.yangbo.seckill.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getServiceById(int id){
        return userDao.getById(id);
    }

    //事务的标签   如果2插入不进去，则1也不能成功
    @Transactional
    public boolean tx() {
        User u1 = new User();
        u1.setId(2);
        u1.setName("tan");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("tantan");
        userDao.insert(u2);

        return true;
    }


}
