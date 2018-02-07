package com.bookstore.service;

import com.bookstore.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User findUserById(Long userId);

    User findByUserName(String userName);

    User createUser(User user);

    void updateUser(User user);

    void deleteUserById(Long userId);
}
