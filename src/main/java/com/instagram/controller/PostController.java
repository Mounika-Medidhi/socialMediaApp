package com.instagram.controller;

import com.instagram.model.Post;
import com.instagram.service.PostService;
import com.instagram.service.PostServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PostController {

    private static final Logger logger =
            LoggerFactory.getLogger(PostController.class);

    private PostService postService;

    public PostController() {
        postService = new PostServiceImpl();
    }


    public boolean createPost(Post post) {

        logger.info(
                "Create post request received: user_id={}",
                post.getUser().getUser_id()
        );

        return postService.createPost(post);
    }


    public List<Post> findPostsByUsername(String username) {

        logger.info(
                "Find posts request received: username={}",
                username
        );

        return postService.findPostsByUsername(username);
    }


    public List<Post> findAllPosts() {

        logger.info(
                "Find all posts request received"
        );

        return postService.findAllPosts();
    }


    public boolean updatePost(Post post) {

        logger.info(
                "Update post request received: post_id={}, username={}",
                post.getPost_id(),
                post.getUser().getUsername()
        );

        return postService.updatePost(post);
    }


    public boolean deletePost(int post_id, String username) {

        logger.info(
                "Delete post request received: post_id={}, username={}",
                post_id,
                username
        );

        return postService.deletePost(
                post_id,
                username
        );
    }
}