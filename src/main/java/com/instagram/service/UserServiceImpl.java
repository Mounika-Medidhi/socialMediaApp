package com.instagram.service;

import com.instagram.dao.UserDAO;
import com.instagram.dao.UserDAOImpl;
import com.instagram.model.User;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl() {
        userDAO = new UserDAOImpl();
    }

    @Override
    public boolean signupUser(User user) {

        if (user == null) {
            return false;
        }

        if (user.getUsername() == null ||
                user.getUsername().trim().isEmpty()) {
            return false;
        }

        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {
            return false;
        }

        if (!isValidPassword(user.getPassword_hash())) {
            return false;
        }

        return userDAO.signUpUser(user);
    }

    @Override
    public User searchUserById(int user_id) {

        if (user_id <= 0) {
            return null;
        }

        User user = userDAO.searchUserById(user_id);

        if (user == null) {
            return null;
        }

        return user;
    }

    @Override
    public User searchUserByUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        return userDAO.searchUserByUsername(username);
    }
    private boolean isValidPassword(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;

        for (char character : password.toCharArray()) {

            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            }

            if (Character.isLowerCase(character)) {
                hasLowercase = true;
            }

            if (Character.isDigit(character)) {
                hasDigit = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercase &&
                hasLowercase &&
                hasDigit &&
                hasSpecialCharacter;
    }
}