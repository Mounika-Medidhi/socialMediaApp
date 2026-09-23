package com.instagram.model;

import java.time.LocalDateTime;

public class Comment {

    private int comment_id;
    private User user;
    private Post post;
    private Comment parent_comment;
    private String comment_text;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Comment() {
    }

    public Comment(int comment_id,
                   User user,
                   Post post,
                   Comment parent_comment,
                   String comment_text,
                   LocalDateTime created_at,
                   LocalDateTime updated_at) {

        this.comment_id = comment_id;
        this.user = user;
        this.post = post;
        this.parent_comment = parent_comment;
        this.comment_text = comment_text;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
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

    public Comment getParent_comment() {
        return parent_comment;
    }

    public void setParent_comment(Comment parent_comment) {
        this.parent_comment = parent_comment;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}