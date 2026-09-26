package com.instagram.controller;

import com.instagram.model.Comment;
import com.instagram.service.CommentService;
import com.instagram.service.CommentServiceImpl;

import java.util.List;

public class CommentController {

    private CommentService commentService;


    public CommentController() {

        commentService = new CommentServiceImpl();
    }


    // CREATE COMMENT
    public boolean createComment(Comment comment) {

        return commentService.createComment(comment);
    }


    // FIND COMMENTS BY POST ID
    public List<Comment> findCommentsByPostId(int post_id) {

        return commentService.findCommentsByPostId(post_id);
    }


    // UPDATE COMMENT
    public boolean updateComment(Comment comment) {

        return commentService.updateComment(comment);
    }


    // DELETE COMMENT
    public boolean deleteComment(Comment comment) {

        return commentService.deleteComment(comment);
    }
}