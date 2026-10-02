package com.instagram.service;

import com.instagram.dao.UserDAO;
import com.instagram.dao.UserDAOImpl;
import com.instagram.exception.UserNotFoundException;
import com.instagram.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.instagram.exception.InvalidPasswordException;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDAO userDAO;

    public UserServiceImpl() {
        userDAO = new UserDAOImpl();
    }

    // Constructor used for JUnit + Mockito testing
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean signupUser(User user) {

        // Check whether User object itself is null
        if (user == null) {

            logger.warn(
                    "Signup failed: user is null"
            );

            throw new IllegalArgumentException(
                    "User cannot be null."
            );
        }

        // Check username
        if (user.getUsername() == null ||
                user.getUsername().trim().isEmpty() ||
                user.getUsername().trim().equalsIgnoreCase("null")) {

            logger.warn(
                    "Signup failed: username is empty or invalid"
            );

            throw new IllegalArgumentException(
                    "Username cannot be empty."
            );
        }

        // Check email
        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {

            logger.warn(
                    "Signup failed: email is empty"
            );

            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        // Check email format
        if (!isValidEmail(user.getEmail())) {

            throw new IllegalArgumentException(
                    "Invalid email format."
            );
        }

        // Check password
        if (user.getPassword_hash() == null ||
                user.getPassword_hash().trim().isEmpty()) {

            logger.warn(
                    "Signup failed: password is empty"
            );

            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        // Check password strength
        if (!isValidPassword(user.getPassword_hash())) {

            throw new InvalidPasswordException(
                    "Password must contain at least 8 characters, " +
                            "one uppercase letter, one lowercase letter, " +
                            "one digit, and one special character."
            );
        }

        // Call DAO only after all validations pass
        boolean result = userDAO.signUpUser(user);

        // Log only when signup is actually successful
        if (result) {

            logger.info(
                    "Signup completed successfully: username={}",
                    user.getUsername()
            );
        }

        return result;
    }

    @Override
    public User searchUserById(int user_id) {

        if (user_id <= 0) {

            logger.warn(
                    "Search failed: invalid user ID={}",
                    user_id
            );

            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        User user =
                userDAO.searchUserById(user_id);

        if (user == null) {

            logger.warn(
                    "Search failed: user not found, user_id={}",
                    user_id
            );

            throw new UserNotFoundException(
                    "No such user exists."
            );
        }

        logger.info(
                "User search successful: user_id={}",
                user_id
        );

        return user;
    }

    @Override
    public User searchUserByUsername(String username) {

        if (username == null ||
                username.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Username cannot be empty"
            );
        }

        username = username.trim();

        User user =
                userDAO.searchUserByUsername(username);

        if (user == null) {

            throw new UserNotFoundException(
                    "No such user exists."
            );
        }

        return user;
    }

    @Override
    public User searchUserByEmail(String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            logger.warn(
                    "Search failed: email is empty"
            );

            throw new IllegalArgumentException(
                    "Email cannot be empty"
            );
        }

        User user =
                userDAO.searchUserByEmail(email);

        if (user == null) {

            logger.warn(
                    "Search failed: user not found, email={}",
                    email
            );

            throw new IllegalArgumentException(
                    "User not found"
            );
        }

        logger.info(
                "User search successful: email={}",
                email
        );

        return user;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users =
                userDAO.getAllUsers();

        logger.info(
                "Retrieved {} users from service layer",
                users.size()
        );

        return users;
    }

    // ==========================================
    // UPDATE USER
    // ==========================================

    @Override
    public boolean updateUser(User user) {

        if (user == null) {

            logger.warn(
                    "Update failed: user is null"
            );

            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        if (user.getUser_id() <= 0) {

            logger.warn(
                    "Update failed: invalid user ID={}",
                    user.getUser_id()
            );

            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        if (user.getUsername() == null ||
                user.getUsername().trim().isEmpty()) {

            logger.warn(
                    "Update failed: username is empty, user_id={}",
                    user.getUser_id()
            );

            throw new IllegalArgumentException(
                    "Username cannot be empty"
            );
        }

        // Validate email is not empty
        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {

            logger.warn(
                    "Update failed: email is empty, user_id={}",
                    user.getUser_id()
            );

            throw new IllegalArgumentException(
                    "Email cannot be empty"
            );
        }

        // Validate email format
        if (!isValidEmail(user.getEmail())) {

            logger.warn(
                    "Update failed: invalid email, user_id={}",
                    user.getUser_id()
            );

            throw new IllegalArgumentException(
                    "Invalid email format"
            );
        }

        // Validate password is not empty
        if (user.getPassword_hash() == null ||
                user.getPassword_hash().trim().isEmpty()) {

            logger.warn(
                    "Update failed: password is empty, user_id={}",
                    user.getUser_id()
            );

            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        // Validate password strength
        if (!isValidPassword(user.getPassword_hash())) {

            logger.warn(
                    "Update failed: invalid password, user_id={}",
                    user.getUser_id()
            );

            throw new IllegalArgumentException(
                    "Invalid password format"
            );
        }

        boolean result =
                userDAO.updateUser(user);

        if (result) {

            logger.info(
                    "User update completed successfully: user_id={}",
                    user.getUser_id()
            );
        }

        return result;
    }

    @Override
    public boolean deleteUser(
            String username,
            String password) {

        if (username == null ||
                username.trim().isEmpty()) {

            logger.warn(
                    "Delete failed: username is empty"
            );

            throw new IllegalArgumentException(
                    "Username cannot be empty"
            );
        }

        if (password == null ||
                password.trim().isEmpty()) {

            logger.warn(
                    "Delete failed: password is empty, username={}",
                    username
            );

            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        User user =
                userDAO.searchUserByUsername(username);

        if (user == null) {

            logger.warn(
                    "Delete failed: user not found, username={}",
                    username
            );

            throw new IllegalArgumentException(
                    "User not found"
            );
        }

        if (!user.getPassword_hash().equals(password.trim())) {

            logger.warn(
                    "Delete failed: invalid password, username={}",
                    username
            );

            throw new IllegalArgumentException(
                    "Invalid password"
            );
        }

        boolean result =
                userDAO.deleteUser(
                        user.getUser_id()
                );

        if (result) {

            logger.info(
                    "User deleted successfully: user_id={}",
                    user.getUser_id()
            );
        }

        return result;
    }

    // =========================
    // ADMIN OPERATIONS
    // =========================

    @Override
    public boolean createAdmin(User user) {

        if (user == null) {

            logger.warn(
                    "Admin creation failed: user is null"
            );

            return false;
        }

        if (user.getUsername() == null ||
                user.getUsername().trim().isEmpty()) {

            logger.warn(
                    "Admin creation failed: username is empty"
            );

            return false;
        }

        if (!isValidEmail(user.getEmail())) {

            logger.warn(
                    "Admin creation failed: invalid email for username={}",
                    user.getUsername()
            );

            return false;
        }

        if (!isValidPassword(user.getPassword_hash())) {

            logger.warn(
                    "Admin creation failed: invalid password for username={}",
                    user.getUsername()
            );

            return false;
        }

        boolean result =
                userDAO.createAdmin(user);

        if (result) {

            logger.info(
                    "Admin created successfully: username={}",
                    user.getUsername()
            );

        } else {

            logger.warn(
                    "Admin creation failed: admin may already exist"
            );
        }

        return result;
    }

    @Override
    public int countTotalUsers() {

        int totalUsers =
                userDAO.countTotalUsers();

        return totalUsers;
    }

    @Override
    public Map<String, Integer> countUsersByStatus() {

        Map<String, Integer> statusCounts =
                userDAO.countUsersByStatus();

        return statusCounts;
    }

    // =========================
    // VALIDATION METHODS
    // =========================

    private boolean isValidEmail(String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            return false;
        }

        int atIndex =
                email.indexOf('@');

        if (atIndex <= 0) {

            return false;
        }

        int dotIndex =
                email.indexOf('.', atIndex);

        if (dotIndex <= atIndex + 1) {

            return false;
        }

        if (dotIndex == email.length() - 1) {

            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {

        if (password == null ||
                password.length() < 8) {

            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;

        for (char character :
                password.toCharArray()) {

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