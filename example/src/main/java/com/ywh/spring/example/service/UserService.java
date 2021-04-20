package com.ywh.spring.example.service;


import com.ywh.spring.example.model.User;

import java.util.List;

/**
 * @author ywh
 * @since 4/20/2021
 */
public interface UserService {
    User getUserById(long id);

    List<User> getUser();

    User addUser(String name);

    int deleteUser(long id);
}
