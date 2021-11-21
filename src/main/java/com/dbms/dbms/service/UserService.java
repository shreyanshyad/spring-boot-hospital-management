package com.dbms.dbms.service;

import com.dbms.dbms.dao.UserDao;
import com.dbms.dbms.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public int createPatient(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println(encodedPassword);
        System.out.println(rawPassword);
        user.setPassword(encodedPassword);
        user.setRole("ROLE_PATIENT");
        return userDao.createUser(user);
    }

    public void createUser(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        user.setPassword(encodedPassword);
        userDao.createUser(user);
    }

    public boolean isProfileComplete(String email) {
        User user = userDao.findUserByEmail(email);
        return user.isInit();
    }

    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }
}
