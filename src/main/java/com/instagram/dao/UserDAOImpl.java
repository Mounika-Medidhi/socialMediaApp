package com.instagram.dao;

import com.instagram.model.User;
import com.instagram.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean signUpUser(User user) {

        String sql = "INSERT INTO users " +
                "(username, email, password_hash) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword_hash());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User searchUserById(int user_id) {

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword_hash(resultSet.getString("password_hash"));
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

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User searchUserByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setUser_id(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword_hash(resultSet.getString("password_hash"));
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

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(int user_id) {
        return false;
    }
}