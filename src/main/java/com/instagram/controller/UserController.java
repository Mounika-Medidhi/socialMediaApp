package com.instagram.controller;

import com.instagram.model.User;
import com.instagram.service.UserService;
import com.instagram.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController() {
        userService = new UserServiceImpl();
    }

    public boolean signupUser(User user) {

        logger.info(
                "Signup request received: username={}",
                user.getUsername()
        );

        return userService.signupUser(user);
    }

    public User searchUserById(int user_id) {

        logger.info(
                "Search user request received: user_id={}",
                user_id
        );

        return userService.searchUserById(user_id);
    }

    public User searchUserByUsername(String username) {

        logger.info(
                "Search user request received: username={}",
                username
        );

        return userService.searchUserByUsername(username);
    }

    public User searchUserByEmail(String email) {

        logger.info(
                "Search user request received: email={}",
                email
        );

        return userService.searchUserByEmail(email);
    }

    public List<User> getAllUsers() {

        logger.info("Get all users request received");

        return userService.getAllUsers();
    }

    public boolean updateUser(User user) {

        logger.info(
                "Update user request received: user_id={}",
                user.getUser_id()
        );

        return userService.updateUser(user);
    }

    public boolean deleteUser(String username, String password) {

        logger.info(
                "Delete user request received: username={}",
                username
        );

        return userService.deleteUser(username, password);
    }
}