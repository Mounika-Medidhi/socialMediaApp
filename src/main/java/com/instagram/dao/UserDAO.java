package com.instagram.dao;

import com.instagram.model.User;

import java.util.List;

public interface UserDAO {

    boolean signUpUser(User user);

    User searchUserById(int user_id);

    User searchUserByUsername(String username);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    boolean updateUser(User user);

    boolean deleteUser(int user_id);
}