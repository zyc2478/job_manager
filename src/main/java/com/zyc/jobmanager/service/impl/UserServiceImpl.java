package com.zyc.jobmanager.service.impl;

import com.zyc.jobmanager.dao.IUserDao;
import com.zyc.jobmanager.entity.User;
import com.zyc.jobmanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements IUserService {

    //@Resource
    @Autowired
    private IUserDao userDao;

    @Override
    public User getUserById(int userId) {
        return this.userDao.selectByPrimaryKey(userId);
    }

    @Override
    public void insertUser(User user) { this.userDao.insert(user); }

}