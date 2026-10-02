package com.instagram.controller;

import com.instagram.model.Profile;
import com.instagram.model.User;
import com.instagram.service.UserService;
import com.instagram.service.UserServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserController {

    private UserService userService;

    public UserController() {
        userService = new UserServiceImpl();
    }
    // SIGNUP USER


    public boolean signupUser(User user) {
        return userService.signupUser(user);
    }


    // SEARCH USER BY ID

    public User searchUserById(int user_id) {
        return userService.searchUserById(user_id);
    }


    // ==========================================
    // SEARCH USER BY USERNAME
    // ==========================================

    public User searchUserByUsername(String username) {
        return userService.searchUserByUsername(username);
    }


    // ==========================================
    // SEARCH USER BY EMAIL
    // ==========================================

    public User searchUserByEmail(String email) {
        return userService.searchUserByEmail(email);
    }


    // ==========================================
    // GET ALL USERS
    // ==========================================

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    // ==========================================
    // UPDATE USER
    // ==========================================

    public boolean updateUser(User user) {
        return userService.updateUser(user);
    }


    // ==========================================
    // DELETE USER
    // ==========================================

    public boolean deleteUser(
            String username,
            String password) {

        return userService.deleteUser(
                username,
                password
        );
    }

    // ==========================================
    // CREATE ADMIN
    // ==========================================

    public boolean createAdmin(User user) {
        return userService.createAdmin(user);
    }

    // ==========================================
    // COUNT TOTAL USERS
    // ==========================================

    public int countTotalUsers() {
        return userService.countTotalUsers();
    }

    // ==========================================
    // COUNT USERS BY STATUS
    // ==========================================

    public Map<String, Integer> countUsersByStatus() {
        return userService.countUsersByStatus();
    }
}