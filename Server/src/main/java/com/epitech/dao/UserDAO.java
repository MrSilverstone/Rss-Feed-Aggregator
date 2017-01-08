package com.epitech.dao;

import com.epitech.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Repository
public class UserDAO implements IUserDAO {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        String hql = "FROM User as u ORDER BY u.id";
        return (List<User>)hibernateTemplate.find(hql);
    }

    @Override
    public User getUserById(long id) {
        return hibernateTemplate.get(User.class, id);
    }

    @Override
    public boolean addUser(User user) {
        hibernateTemplate.save(user);
        return true;
    }

    @Override
    public void updateUser(User user) {
        User u = getUserById(user.getId());
        u.setEmail(user.getEmail());
        u.setName(user.getName());
        hibernateTemplate.update(u);
    }

    @Override
    public void deleteUser(long id) {
        hibernateTemplate.delete(getUserById(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean userExists(String email) {
        String hql = "FROM User as u WHERE u.email = ?";
        List<User> users = (List<User>)hibernateTemplate.find(hql, email);

        return users.size() > 0;
    }
}
