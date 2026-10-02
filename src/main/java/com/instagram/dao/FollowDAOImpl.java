package com.instagram.dao;

import com.instagram.model.Follow;
import com.instagram.model.User;
import com.instagram.util.JDBCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FollowDAOImpl implements FollowDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(FollowDAOImpl.class);

    private static final String CREATE_FOLLOW =
            "INSERT INTO follows " +
                    "(follower_id, following_id) " +
                    "VALUES (?, ?)";

    private static final String FIND_FOLLOWERS_BY_USERNAME =
            "SELECT u.* " +
                    "FROM follows f " +
                    "JOIN users u ON f.follower_id = u.user_id " +
                    "JOIN users target ON f.following_id = target.user_id " +
                    "WHERE target.username = ?";

    private static final String FIND_FOLLOWING_BY_USERNAME =
            "SELECT u.* " +
                    "FROM follows f " +
                    "JOIN users target ON f.follower_id = target.user_id " +
                    "JOIN users u ON f.following_id = u.user_id " +
                    "WHERE target.username = ?";

    private static final String DELETE_FOLLOW =
            "DELETE FROM follows " +
                    "WHERE follower_id = ? " +
                    "AND following_id = ?";


    @Override
    public boolean createFollow(Follow follow) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(CREATE_FOLLOW)) {

            statement.setInt(
                    1,
                    follow.getFollower().getUser_id()
            );

            statement.setInt(
                    2,
                    follow.getFollowing().getUser_id()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                /*
                logger.info(
                        "Follow created successfully: follower_id={}, following_id={}",
                        follow.getFollower().getUser_id(),
                        follow.getFollowing().getUser_id()
                );
                */

                return true;
            }

            /*
            logger.warn(
                    "Follow creation failed: follower_id={}, following_id={}",
                    follow.getFollower().getUser_id(),
                    follow.getFollowing().getUser_id()
            );
            */

        } catch (Exception e) {

            logger.error(
                    "Error while creating follow: follower_id={}, following_id={}",
                    follow.getFollower().getUser_id(),
                    follow.getFollowing().getUser_id(),
                    e
            );
        }

        return false;
    }


    @Override
    public List<Follow> findFollowersByUsername(String username) {

        List<Follow> follows = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_FOLLOWERS_BY_USERNAME)) {

            statement.setString(1, username);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Follow follow = new Follow();

                User follower = new User();

                follower.setUser_id(
                        resultSet.getInt("user_id")
                );

                follower.setUsername(
                        resultSet.getString("username")
                );

                follower.setEmail(
                        resultSet.getString("email")
                );

                follower.setStatus(
                        resultSet.getString("status")
                );

                follower.setRole(
                        resultSet.getString("role")
                );

                if (resultSet.getTimestamp("created_at") != null) {

                    follower.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    follower.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                follow.setFollower(follower);

                follows.add(follow);
            }

            /*
            logger.info(
                    "Retrieved {} followers for username={}",
                    follows.size(),
                    username
            );
            */

        } catch (Exception e) {

            logger.error(
                    "Error while finding followers for username={}",
                    username,
                    e
            );
        }

        return follows;
    }


    @Override
    public List<Follow> findFollowingByUsername(String username) {

        List<Follow> follows = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_FOLLOWING_BY_USERNAME)) {

            statement.setString(
                    1,
                    username
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Follow follow = new Follow();

                /*
                 * This User is the person being followed.
                 */
                User following = new User();

                following.setUser_id(
                        resultSet.getInt("user_id")
                );

                following.setUsername(
                        resultSet.getString("username")
                );

                following.setEmail(
                        resultSet.getString("email")
                );

                following.setStatus(
                        resultSet.getString("status")
                );

                following.setRole(
                        resultSet.getString("role")
                );

                if (resultSet.getTimestamp("created_at") != null) {

                    following.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    following.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                follow.setFollowing(following);

                follows.add(follow);
            }

            /*
            logger.info(
                    "Retrieved {} following users for username={}",
                    follows.size(),
                    username
            );
            */

        } catch (Exception e) {

            logger.error(
                    "Error while finding following users for username={}",
                    username,
                    e
            );
        }

        return follows;
    }


    @Override
    public boolean deleteFollow(
            int follower_id,
            int following_id) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_FOLLOW)) {

            statement.setInt(1, follower_id);
            statement.setInt(2, following_id);

            int rows = statement.executeUpdate();

            if (rows > 0) {

                /*
                logger.info(
                        "Follow deleted successfully: follower_id={}, following_id={}",
                        follower_id,
                        following_id
                );
                */

                return true;
            }

            /*
            logger.warn(
                    "No follow relationship found: follower_id={}, following_id={}",
                    follower_id,
                    following_id
            );
            */

        } catch (Exception e) {

            logger.error(
                    "Error while deleting follow: follower_id={}, following_id={}",
                    follower_id,
                    following_id,
                    e
            );
        }

        return false;
    }
}