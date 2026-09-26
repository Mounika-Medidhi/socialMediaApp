package com.instagram.dao;

import com.instagram.model.Comment;

import java.util.List;

public interface CommentDAO {

    // CREATE
    boolean createComment(Comment comment);

    // READ
    List<Comment> findCommentsByPostId(int post_id);

    // UPDATE
    boolean updateComment(Comment comment);

    // DELETE
    boolean deleteComment(Comment comment);
}