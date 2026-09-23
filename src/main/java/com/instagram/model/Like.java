package com.instagram.model;

import java.time.LocalDateTime;

public class Like {

    private int like_id;
    private User user;
    private Post post;
    private LocalDateTime created_at;

    public Like() {
    }

    public Like(int like_id,
                User user,
                Post post,
                LocalDateTime created_at) {

        this.like_id = like_id;
        this.user = user;
        this.post = post;
        this.created_at = created_at;
    }

    public int getLike_id() {
        return like_id;
    }

    public void setLike_id(int like_id) {
        this.like_id = like_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}