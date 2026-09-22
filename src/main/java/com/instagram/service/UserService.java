package com.instagram.service;

import com.instagram.model.User;

public interface UserService {

    boolean signupUser(User user);

    User searchUserById(int user_id);

    User searchUserByUsername(String username);

}