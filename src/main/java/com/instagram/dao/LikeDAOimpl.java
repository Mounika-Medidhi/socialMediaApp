package com.instagram.dao;

import com.instagram.model.Like;
import com.instagram.model.Post;
import com.instagram.model.User;
import com.instagram.util.JDBCUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LikeDAOimpl implements LikeDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(LikeDAOimpl.class);

    private static final String CREATE_LIKE =
            "INSERT INTO likes " +
                    "(user_id, post_id) " +
                    "VALUES (?, ?)";

    private static final String FIND_LIKES_BY_POST_ID =
            "SELECT l.*, u.username " +
                    "FROM likes l " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "WHERE l.post_id = ?";

    private static final String DELETE_LIKE =
            "DELETE FROM likes " +
                    "WHERE user_id = ? " +
                    "AND post_id = ?";

    private static final String COUNT_LIKES_BY_POST_ID =
            "SELECT COUNT(*) FROM likes WHERE post_id = ?";


    @Override
    public boolean createLike(Like like) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(CREATE_LIKE)) {

            statement.setInt(
                    1,
                    like.getUser().getUser_id()
            );

            statement.setInt(
                    2,
                    like.getPost().getPost_id()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {
                return true;
            }

        } catch (Exception e) {

            logger.error(
                    "Error while creating like: user_id={}, post_id={}",
                    like.getUser().getUser_id(),
                    like.getPost().getPost_id(),
                    e
            );
        }

        return false;
    }


    @Override
    public List<Like> findLikesByPostId(int post_id) {

        List<Like> likes = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_LIKES_BY_POST_ID)) {

            statement.setInt(
                    1,
                    post_id
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Like like = new Like();

                like.setLike_id(
                        resultSet.getInt("like_id")
                );

                User user = new User();

                user.setUser_id(
                        resultSet.getInt("user_id")
                );

                user.setUsername(
                        resultSet.getString("username")
                );

                like.setUser(user);

                Post post = new Post();

                post.setPost_id(
                        resultSet.getInt("post_id")
                );

                like.setPost(post);

                if (resultSet.getTimestamp("created_at") != null) {

                    like.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                likes.add(like);
            }

        } catch (Exception e) {

            logger.error(
                    "Error while finding likes for post_id={}",
                    post_id,
                    e
            );
        }

        return likes;
    }


    @Override
    public boolean deleteLike(int user_id, int post_id) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_LIKE)) {

            statement.setInt(1, user_id);
            statement.setInt(2, post_id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                return true;
            }

        } catch (Exception e) {

            logger.error(
                    "Error while deleting like: user_id={}, post_id={}",
                    user_id,
                    post_id,
                    e
            );
        }

        return false;
    }


    @Override
    public int countLikesByPostId(int post_id) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(COUNT_LIKES_BY_POST_ID)) {

            statement.setInt(1, post_id);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return resultSet.getInt(1);
            }

        } catch (Exception e) {

            logger.error(
                    "Error while counting likes: post_id={}",
                    post_id,
                    e
            );
        }

        return 0;
    }
}