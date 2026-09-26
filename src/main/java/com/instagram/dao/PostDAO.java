package com.instagram.dao;

import com.instagram.model.Post;

import java.util.List;

public interface PostDAO {

    boolean createPost(Post post);

    List<Post> findPostsByUsername(String username);

    List<Post> findAllPosts();

    boolean updatePost(Post post);

    boolean deletePost(int post_id, String username);
}