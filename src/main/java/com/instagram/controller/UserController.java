package com.instagram.controller;

import com.instagram.model.User;
import com.instagram.service.UserService;
import com.instagram.service.UserServiceImpl;

import java.util.List;

public class UserController {

    private UserService userService;

    public UserController() {
        userService = new UserServiceImpl();
    }

    public boolean signupUser(User user) {
        return userService.signupUser(user);
    }

    public User searchUserById(int user_id) {
        return userService.searchUserById(user_id);
    }

    public User searchUserByUsername(String username) {
        return userService.searchUserByUsername(username);
    }

    public User searchUserByEmail(String email) {
        return userService.searchUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}