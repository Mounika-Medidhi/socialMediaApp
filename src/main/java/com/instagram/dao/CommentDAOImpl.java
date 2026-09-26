package com.instagram.dao;

import com.instagram.model.Comment;
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

public class CommentDAOImpl implements CommentDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(CommentDAOImpl.class);


    // SQL QUERY FOR CREATE COMMENT
    private static final String CREATE_COMMENT =
            "INSERT INTO comments " +
                    "(post_id, user_id, comment_text) " +
                    "VALUES (?, ?, ?)";


    // SQL QUERY FOR FINDING COMMENTS BY POST ID
    private static final String FIND_COMMENTS_BY_POST_ID =
            "SELECT * FROM comments " +
                    "WHERE post_id = ? " +
                    "ORDER BY created_at";


    // SQL QUERY FOR UPDATE COMMENT
    private static final String UPDATE_COMMENT =
            "UPDATE comments " +
                    "SET comment_text = ? " +
                    "WHERE comment_id = ? " +
                    "AND user_id = ?";


    // SQL QUERY FOR DELETE COMMENT
    private static final String DELETE_COMMENT =
            "DELETE FROM comments " +
                    "WHERE comment_id = ? " +
                    "AND user_id = ?";


    // CREATE COMMENT
    @Override
    public boolean createComment(Comment comment) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(CREATE_COMMENT)) {

            statement.setInt(
                    1,
                    comment.getPost().getPost_id()
            );

            statement.setInt(
                    2,
                    comment.getUser().getUser_id()
            );

            statement.setString(
                    3,
                    comment.getComment_text()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Comment created successfully for post_id={}",
                        comment.getPost().getPost_id()
                );

                return true;
            }

            logger.warn(
                    "Comment creation failed for post_id={}",
                    comment.getPost().getPost_id()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while creating comment for post_id={}",
                    comment.getPost().getPost_id(),
                    e
            );
        }

        return false;
    }


    // FIND COMMENTS BY POST ID
    @Override
    public List<Comment> findCommentsByPostId(int post_id) {

        List<Comment> comments = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_COMMENTS_BY_POST_ID)) {

            statement.setInt(1, post_id);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                Comment comment = new Comment();

                comment.setComment_id(
                        resultSet.getInt("comment_id")
                );


                // Create Post object
                Post post = new Post();

                post.setPost_id(
                        resultSet.getInt("post_id")
                );

                comment.setPost(post);


                // Create User object
                User user = new User();

                user.setUser_id(
                        resultSet.getInt("user_id")
                );

                comment.setUser(user);


                comment.setComment_text(
                        resultSet.getString("comment_text")
                );


                if (resultSet.getTimestamp("created_at") != null) {

                    comment.setCreated_at(
                            resultSet
                                    .getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }


                if (resultSet.getTimestamp("updated_at") != null) {

                    comment.setUpdated_at(
                            resultSet
                                    .getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }


                comments.add(comment);
            }

            logger.info(
                    "Retrieved {} comments for post_id={}",
                    comments.size(),
                    post_id
            );

        } catch (Exception e) {

            logger.error(
                    "Error while finding comments for post_id={}",
                    post_id,
                    e
            );
        }

        return comments;
    }


    // UPDATE COMMENT
    @Override
    public boolean updateComment(Comment comment) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_COMMENT)) {

            // New comment text
            statement.setString(
                    1,
                    comment.getComment_text()
            );

            // Comment ID
            statement.setInt(
                    2,
                    comment.getComment_id()
            );

            // Logged-in user's ID
            statement.setInt(
                    3,
                    comment.getUser().getUser_id()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Comment updated successfully: comment_id={}, user_id={}",
                        comment.getComment_id(),
                        comment.getUser().getUser_id()
                );

                return true;
            }

            logger.warn(
                    "Comment update failed or user is not the owner: " +
                            "comment_id={}, user_id={}",
                    comment.getComment_id(),
                    comment.getUser().getUser_id()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while updating comment_id={}, user_id={}",
                    comment.getComment_id(),
                    comment.getUser().getUser_id(),
                    e
            );
        }

        return false;
    }


    // DELETE COMMENT
    @Override
    public boolean deleteComment(Comment comment) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_COMMENT)) {

            // Comment ID
            statement.setInt(
                    1,
                    comment.getComment_id()
            );

            // Logged-in user's ID
            statement.setInt(
                    2,
                    comment.getUser().getUser_id()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Comment deleted successfully: comment_id={}, user_id={}",
                        comment.getComment_id(),
                        comment.getUser().getUser_id()
                );

                return true;
            }

            logger.warn(
                    "Comment deletion failed or user is not the owner: " +
                            "comment_id={}, user_id={}",
                    comment.getComment_id(),
                    comment.getUser().getUser_id()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while deleting comment_id={}, user_id={}",
                    comment.getComment_id(),
                    comment.getUser().getUser_id(),
                    e
            );
        }

        return false;
    }
}