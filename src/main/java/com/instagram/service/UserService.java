package com.instagram.service;

import com.instagram.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    boolean signupUser(User user);

    User searchUserById(int user_id);

    User searchUserByUsername(String username);

    User searchUserByEmail(String email);

    List<User> getAllUsers();

    boolean updateUser(User user);

    boolean deleteUser(String username, String password);

    // Admin operations
    boolean createAdmin(User user);

    int countTotalUsers();

    Map<String, Integer> countUsersByStatus();
}