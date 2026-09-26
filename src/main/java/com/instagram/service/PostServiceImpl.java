package com.instagram.service;

import com.instagram.dao.PostDAO;
import com.instagram.dao.PostDAOImpl;
import com.instagram.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PostServiceImpl implements PostService {

    private static final Logger logger =
            LoggerFactory.getLogger(PostServiceImpl.class);

    private PostDAO postDAO;

    public PostServiceImpl() {
        postDAO = new PostDAOImpl();
    }

    @Override
    public boolean createPost(Post post) {

        if (post == null) {

            logger.warn(
                    "Post creation failed: post is null"
            );

            return false;
        }

        if (post.getUser() == null) {

            logger.warn(
                    "Post creation failed: user is null"
            );

            return false;
        }

        if (post.getUser().getUser_id() <= 0) {

            logger.warn(
                    "Post creation failed: invalid user_id={}",
                    post.getUser().getUser_id()
            );

            return false;
        }

        boolean captionEmpty =
                post.getCaption() == null ||
                        post.getCaption().trim().isEmpty();

        boolean imageEmpty =
                post.getImage_url() == null ||
                        post.getImage_url().trim().isEmpty();

        if (captionEmpty && imageEmpty) {

            logger.warn(
                    "Post creation failed: caption and image are both empty"
            );

            return false;
        }

        return postDAO.createPost(post);
    }


    @Override
    public List<Post> findPostsByUsername(String username) {

        if (username == null ||
                username.trim().isEmpty()) {

            logger.warn(
                    "Post search failed: username is empty"
            );

            return List.of();
        }

        List<Post> posts =
                postDAO.findPostsByUsername(username);

        if (posts.isEmpty()) {

            logger.warn(
                    "No posts found for username={}",
                    username
            );

            return posts;
        }

        logger.info(
                "Posts found successfully for username={}: count={}",
                username,
                posts.size()
        );

        return posts;
    }


    @Override
    public List<Post> findAllPosts() {

        List<Post> posts =
                postDAO.findAllPosts();

        if (posts.isEmpty()) {

            logger.warn(
                    "No posts found"
            );

            return posts;
        }

        logger.info(
                "Retrieved {} posts successfully",
                posts.size()
        );

        return posts;
    }


    @Override
    public boolean updatePost(Post post) {

        if (post == null) {

            logger.warn(
                    "Post update failed: post is null"
            );

            return false;
        }

        if (post.getPost_id() <= 0) {

            logger.warn(
                    "Post update failed: invalid post_id={}",
                    post.getPost_id()
            );

            return false;
        }

        if (post.getUser() == null) {

            logger.warn(
                    "Post update failed: user is null"
            );

            return false;
        }

        if (post.getUser().getUsername() == null ||
                post.getUser().getUsername().trim().isEmpty()) {

            logger.warn(
                    "Post update failed: username is empty"
            );

            return false;
        }

        boolean captionEmpty =
                post.getCaption() == null ||
                        post.getCaption().trim().isEmpty();

        boolean imageEmpty =
                post.getImage_url() == null ||
                        post.getImage_url().trim().isEmpty();

        if (captionEmpty && imageEmpty) {

            logger.warn(
                    "Post update failed: caption and image are both empty"
            );

            return false;
        }

        return postDAO.updatePost(post);
    }


    @Override
    public boolean deletePost(int post_id, String username) {

        if (post_id <= 0) {

            logger.warn(
                    "Post deletion failed: invalid post_id={}",
                    post_id
            );

            return false;
        }

        if (username == null ||
                username.trim().isEmpty()) {

            logger.warn(
                    "Post deletion failed: username is empty"
            );

            return false;
        }

        return postDAO.deletePost(
                post_id,
                username
        );
    }
}