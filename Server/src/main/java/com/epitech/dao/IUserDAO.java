package com.epitech.dao;


import com.epitech.entity.User;

import java.util.List;

public interface IUserDAO {
    List<User> getAllUsers();
    User getUserById(long id);
    boolean addUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    boolean userExists(String email);
}
 