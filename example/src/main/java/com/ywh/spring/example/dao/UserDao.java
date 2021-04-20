package com.ywh.spring.example.dao;


import com.ywh.spring.example.model.User;

import java.util.List;

/**
 * @author ywh
 * @since 4/20/2021
 */
public interface UserDao {
    User get(long id);

    List<User> getAll();

    User save(String name);

    int delete(long id);
}
