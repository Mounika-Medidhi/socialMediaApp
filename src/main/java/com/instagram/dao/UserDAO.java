package com.instagram.dao;

import com.instagram.model.User;
import java.util.List;
import java.util.Map;

public interface UserDAO {

    boolean signUpUser(User user);

    boolean createAdmin(User user);

    User searchUserById(int user_id);

    User searchUserByUsername(String username);

    User searchUserByEmail(String email);

    List<User> getAllUsers();

    boolean updateUser(User user);

    boolean deleteUser(int user_id);

    // Admin operations
    int countTotalUsers();

    Map<String, Integer> countUsersByStatus();
}