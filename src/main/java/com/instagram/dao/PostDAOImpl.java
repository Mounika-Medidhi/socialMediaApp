package com.instagram.dao;

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

public class PostDAOImpl implements PostDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(PostDAOImpl.class);

    private static final String CREATE_POST =
            "INSERT INTO posts " +
                    "(user_id, caption, image_url) " +
                    "VALUES (?, ?, ?)";

    private static final String FIND_POSTS_BY_USERNAME =
            "SELECT p.* " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.user_id " +
                    "WHERE u.username = ?";

    private static final String FIND_ALL_POSTS =
            "SELECT * FROM posts";

    private static final String UPDATE_POST =
            "UPDATE posts p " +
                    "JOIN users u ON p.user_id = u.user_id " +
                    "SET p.caption = ?, p.image_url = ? " +
                    "WHERE p.post_id = ? " +
                    "AND u.username = ?";

    private static final String DELETE_POST =
            "DELETE p " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.user_id " +
                    "WHERE p.post_id = ? " +
                    "AND u.username = ?";


    @Override
    public boolean createPost(Post post) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(CREATE_POST)) {

            statement.setInt(
                    1,
                    post.getUser().getUser_id()
            );

            statement.setString(
                    2,
                    post.getCaption()
            );

            statement.setString(
                    3,
                    post.getImage_url()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Post created successfully: user_id={}",
                        post.getUser().getUser_id()
                );

                return true;
            }

            logger.warn(
                    "Post creation failed: user_id={}",
                    post.getUser().getUser_id()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while creating post: user_id={}",
                    post.getUser().getUser_id(),
                    e
            );
        }

        return false;
    }


    @Override
    public List<Post> findPostsByUsername(String username) {

        List<Post> posts = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_POSTS_BY_USERNAME)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Post post = new Post();

                post.setPost_id(
                        resultSet.getInt("post_id")
                );

                User user = new User();

                user.setUser_id(
                        resultSet.getInt("user_id")
                );

                post.setUser(user);

                post.setCaption(
                        resultSet.getString("caption")
                );

                post.setImage_url(
                        resultSet.getString("image_url")
                );

                post.setStatus(
                        resultSet.getString("status")
                );

                if (resultSet.getTimestamp("created_at") != null) {

                    post.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    post.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                posts.add(post);
            }

            logger.info(
                    "Retrieved {} posts for username={}",
                    posts.size(),
                    username
            );

        } catch (Exception e) {

            logger.error(
                    "Error while finding posts for username={}",
                    username,
                    e
            );
        }

        return posts;
    }


    @Override
    public List<Post> findAllPosts() {

        List<Post> posts = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_POSTS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Post post = new Post();

                post.setPost_id(
                        resultSet.getInt("post_id")
                );

                User user = new User();

                user.setUser_id(
                        resultSet.getInt("user_id")
                );

                post.setUser(user);

                post.setCaption(
                        resultSet.getString("caption")
                );

                post.setImage_url(
                        resultSet.getString("image_url")
                );

                post.setStatus(
                        resultSet.getString("status")
                );

                if (resultSet.getTimestamp("created_at") != null) {

                    post.setCreated_at(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {

                    post.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                posts.add(post);
            }

            logger.info(
                    "Retrieved {} posts successfully",
                    posts.size()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while retrieving all posts",
                    e
            );
        }

        return posts;
    }


    @Override
    public boolean updatePost(Post post) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_POST)) {

            statement.setString(
                    1,
                    post.getCaption()
            );

            statement.setString(
                    2,
                    post.getImage_url()
            );

            statement.setInt(
                    3,
                    post.getPost_id()
            );

            statement.setString(
                    4,
                    post.getUser().getUsername()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Post updated successfully: post_id={}, username={}",
                        post.getPost_id(),
                        post.getUser().getUsername()
                );

                return true;
            }

            logger.warn(
                    "No post updated: post_id={}, username={}",
                    post.getPost_id(),
                    post.getUser().getUsername()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while updating post: post_id={}, username={}",
                    post.getPost_id(),
                    post.getUser().getUsername(),
                    e
            );
        }

        return false;
    }


    @Override
    public boolean deletePost(int post_id, String username) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(DELETE_POST)) {

            statement.setInt(
                    1,
                    post_id
            );

            statement.setString(
                    2,
                    username
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Post deleted successfully: post_id={}, username={}",
                        post_id,
                        username
                );

                return true;
            }

            logger.warn(
                    "No post deleted: post_id={}, username={}",
                    post_id,
                    username
            );

        } catch (Exception e) {

            logger.error(
                    "Error while deleting post: post_id={}, username={}",
                    post_id,
                    username,
                    e
            );
        }

        return false;
    }
}