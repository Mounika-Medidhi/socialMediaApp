package com.instagram.controller;

import com.instagram.model.Post;
import com.instagram.service.PostService;
import com.instagram.service.PostServiceImpl;

import java.util.List;

public class PostController {

    private PostService postService;

    public PostController() {
        postService = new PostServiceImpl();
    }


    // CREATE POST
    public boolean createPost(Post post) {

        return postService.createPost(post);
    }


    // FIND POSTS BY USERNAME
    public List<Post> findPostsByUsername(String username) {

        return postService.findPostsByUsername(username);
    }


    // FIND ALL POSTS
    public List<Post> findAllPosts() {

        return postService.findAllPosts();
    }


    // UPDATE POST
    public boolean updatePost(Post post) {

        return postService.updatePost(post);
    }


    // DELETE POST
    public boolean deletePost(int post_id, String username) {

        return postService.deletePost(
                post_id,
                username
        );
    }
}