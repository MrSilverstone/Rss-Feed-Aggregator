package com.epitech.service;

import com.epitech.dao.IUserDAO;
import com.epitech.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserDAO userDAO;

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User getUserById(long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public synchronized boolean addUser(User user) {
        if (userDAO.userExists(user.getEmail())) {
            return false;
        } else {
            return userDAO.addUser(user);
        }
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(long id) {
        userDAO.deleteUser(id);
    }
}
