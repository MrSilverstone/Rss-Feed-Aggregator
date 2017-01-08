package com.epitech.service;

import com.epitech.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(long id);
    boolean addUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
}
