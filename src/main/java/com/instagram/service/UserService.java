package com.instagram.service;

import com.instagram.model.User;

import java.util.List;

public interface UserService {

    boolean signupUser(User user);

    User searchUserById(int user_id);

    User searchUserByUsername(String username);

    User searchUserByEmail(String email);

    List<User> getAllUsers();
}