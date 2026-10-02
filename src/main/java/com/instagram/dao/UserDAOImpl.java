package com.instagram.dao;
import com.instagram.model.User;
import com.instagram.util.JDBCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDAOImpl.class);

    // =========================
    // SQL QUERIES
    // =========================

    private static final String SIGN_UP_USER =
            "INSERT INTO users " +
                    "(username, email, password_hash) " +
                    "VALUES (?, ?, ?)";

    private static final String CREATE_ADMIN =
            "INSERT INTO users " +
                    "(username, email, password_hash, role) " +
                    "VALUES (?, ?, ?, 'ADMIN')";

    private static final String COUNT_ADMINS =
            "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'";

    private static final String SEARCH_USER_BY_ID =
            "SELECT * FROM users WHERE user_id = ?";

    private static final String SEARCH_USER_BY_USERNAME =
            "SELECT * FROM users WHERE username = ?";

    private static final String SEARCH_USER_BY_EMAIL =
            "SELECT user_id, username, email, status, role, " +
                    "created_at, updated_at " +
                    "FROM users WHERE email = ?";

    private static final String GET_ALL_USERS =
            "SELECT user_id, username, email, status, role, " +
                    "created_at, updated_at " +
                    "FROM users";

    private static final String UPDATE_USER =
            "UPDATE users " +
                    "SET username = ?, email = ?, password_hash = ? " +
                    "WHERE user_id = ?";

    private static final String DELETE_USER =
            "DELETE FROM users WHERE user_id = ?";

    private static final String COUNT_TOTAL_USERS =
            "SELECT COUNT(*) FROM users";

    private static final String COUNT_USERS_BY_STATUS =
            "SELECT status, COUNT(*) " +
                    "FROM users " +
                    "GROUP BY status";


    // =========================
    // SIGN UP USER
    // =========================

    @Override
    public boolean signUpUser(User user) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SIGN_UP_USER)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword_hash());
           // statement.setInt(4, user.getUser_id());

            int rows = statement.executeUpdate();

            if (rows > 0) {

                /*
                logger.info(
                        "User registered successfully: username={}",
                        user.getUsername()
                );
                */

                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {

           /* logger.warn(
                    "Signup failed: username or email already exists"
            );*/

            return false;

        } catch (Exception e) {

            logger.error(
                    "Error while registering user: username={}",
                    user.getUsername(),
                    e
            );
        }

        return false;
    }


    // =========================
    // CREATE ADMIN
    // =========================

    @Override
    public boolean createAdmin(User user) {

        Connection connection = null;

        try {

            connection = JDBCUtil.getConnection();

            // Start transaction
            connection.setAutoCommit(false);

            // Check whether an admin already exists
            try (PreparedStatement countStatement =
                         connection.prepareStatement(COUNT_ADMINS);
                 ResultSet resultSet = countStatement.executeQuery()) {

                if (resultSet.next()) {

                    int adminCount = resultSet.getInt(1);

                    if (adminCount > 0) {

                        logger.warn(
                                "Admin creation failed: admin already exists"
                        );

                        connection.rollback();

                        return false;
                    }
                }
            }

            // Create the first admin
            try (PreparedStatement statement =
                         connection.prepareStatement(CREATE_ADMIN)) {

                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword_hash());

                int rows = statement.executeUpdate();

                if (rows > 0) {

                    connection.commit();

                    logger.info(
                            "Admin created successfully: username={}",
                            user.getUsername()
                    );

                    return true;
                }
            }

            // Insert failed
            connection.rollback();

        } catch (Exception e) {

            if (connection != null) {

                try {

                    connection.rollback();

                } catch (Exception rollbackException) {

                    logger.error(
                            "Error while rolling back admin creation",
                            rollbackException
                    );
                }
            }

            logger.error(
                    "Error while creating admin: username={}",
                    user.getUsername(),
                    e
            );

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);
                    connection.close();

                } catch (Exception e) {

                    logger.error(
                            "Error while closing admin connection",
                            e
                    );
                }
            }
        }

        return false;
    }


    // =========================
    // SEARCH USER BY ID
    // =========================

    @Override
    public User searchUserById(int user_id) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SEARCH_USER_BY_ID)) {

            statement.setInt(1, user_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));

                user.setPassword_hash(
                        resultSet.getString("password_hash")
                );

                user.setStatus(resultSet.getString("status"));
                user.setRole(resultSet.getString("role"));

                if (resultSet.getTimestamp("created_at") != null) {

                    user.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    user.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                logger.info(
                        "User found successfully: user_id={}",
                        user_id
                );

                return user;
            }

            logger.warn(
                    "User not found: user_id={}",
                    user_id
            );

        } catch (Exception e) {

            logger.error(
                    "Error while searching user: user_id={}",
                    user_id,
                    e
            );
        }

        return null;
    }


    // =========================
    // SEARCH USER BY USERNAME
    // =========================

    @Override
    public User searchUserByUsername(String username) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SEARCH_USER_BY_USERNAME)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));

                user.setPassword_hash(
                        resultSet.getString("password_hash")
                );

                user.setStatus(resultSet.getString("status"));
                user.setRole(resultSet.getString("role"));

                if (resultSet.getTimestamp("created_at") != null) {

                    user.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    user.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                return user;
            }

            // Commented because "user not found" is handled
            // by custom exception in the controller.
            /*
            logger.warn(
                    "User not found: username={}",
                    username
            );
            */

        } catch (Exception e) {

            logger.error(
                    "Error while searching user: username={}",
                    username,
                    e
            );
        }

        return null;
    }


    // =========================
    // SEARCH USER BY EMAIL
    // =========================

    @Override
    public User searchUserByEmail(String email) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SEARCH_USER_BY_EMAIL)) {

            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setStatus(resultSet.getString("status"));
                user.setRole(resultSet.getString("role"));

                if (resultSet.getTimestamp("created_at") != null) {

                    user.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    user.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                logger.info(
                        "User found successfully: email={}",
                        email
                );

                return user;
            }

            logger.warn(
                    "User not found: email={}",
                    email
            );

        } catch (Exception e) {

            logger.error(
                    "Error while searching user: email={}",
                    email,
                    e
            );
        }

        return null;
    }


    // =========================
    // GET ALL USERS
    // =========================

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(GET_ALL_USERS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                User user = new User();

                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setStatus(resultSet.getString("status"));
                user.setRole(resultSet.getString("role"));

                if (resultSet.getTimestamp("created_at") != null) {

                    user.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    user.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                users.add(user);
            }

            logger.info(
                    "Retrieved {} users successfully",
                    users.size()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while retrieving all users",
                    e
            );
        }

        return users;
    }


    // =========================
    // UPDATE USER
    // =========================

    @Override
    public boolean updateUser(User user) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_USER)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword_hash());
            statement.setInt(4, user.getUser_id());

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "User updated successfully: user_id={}",
                        user.getUser_id()
                );

                return true;
            }

            logger.warn(
                    "No user updated: user_id={}",
                    user.getUser_id()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while updating user: user_id={}",
                    user.getUser_id(),
                    e
            );
        }

        return false;
    }

    // =========================
    // DELETE USER
    // =========================

    @Override
    public boolean deleteUser(int user_id) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_USER)) {

            statement.setInt(1, user_id);

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "User deleted successfully: user_id={}",
                        user_id
                );

                return true;
            }

            logger.warn(
                    "No user deleted: user_id={}",
                    user_id
            );

        } catch (Exception e) {

            logger.error(
                    "Error while deleting user: user_id={}",
                    user_id,
                    e
            );
        }

        return false;
    }


    // =========================
    // COUNT TOTAL USERS
    // =========================

    @Override
    public int countTotalUsers() {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(COUNT_TOTAL_USERS);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {

                int totalUsers = resultSet.getInt(1);

                /*
                logger.info(
                        "Total users count retrieved: {}",
                        totalUsers
                );
                */

                return totalUsers;
            }

        } catch (Exception e) {

            logger.error(
                    "Error while counting total users",
                    e
            );
        }

        return 0;
    }


    // =========================
    // COUNT USERS BY STATUS
    // =========================

    @Override
    public Map<String, Integer> countUsersByStatus() {

        Map<String, Integer> statusCounts =
                new HashMap<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             COUNT_USERS_BY_STATUS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                String status =
                        resultSet.getString("status");

                int count =
                        resultSet.getInt(2);

                statusCounts.put(status, count);
            }

            /*
            logger.info(
                    "User status counts retrieved successfully"
            );
            */

        } catch (Exception e) {

            logger.error(
                    "Error while counting users by status",
                    e
            );
        }

        return statusCounts;
    }
}
