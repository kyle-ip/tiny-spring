package com.ywh.spring.example.service.impl;


import com.ywh.spring.core.annotation.Service;
import com.ywh.spring.example.dao.UserDao;
import com.ywh.spring.example.model.User;
import com.ywh.spring.example.service.UserService;
import com.ywh.spring.ioc.Autowired;

import java.util.List;

/**
 * @author ywh
 * @since 4/20/2021
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(long id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getUser() {
        return userDao.getAll();
    }

    @Override
    public User addUser(String name) {
        return userDao.save(name);
    }

    @Override
    public int deleteUser(long id) {
        return userDao.delete(id);
    }
}
