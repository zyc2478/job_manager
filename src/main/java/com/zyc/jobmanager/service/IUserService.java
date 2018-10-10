package com.zyc.jobmanager.service;

import com.zyc.jobmanager.entity.User;

public interface IUserService {

    User getUserById(int userId);
    void insertUser(User user);
}