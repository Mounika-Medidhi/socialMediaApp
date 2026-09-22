package com.instagram.controller;

import com.instagram.model.User;
import com.instagram.service.UserService;
import com.instagram.service.UserServiceImpl;

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
}