package com.instagram.service;

import com.instagram.dao.CommentDAO;
import com.instagram.dao.CommentDAOImpl;
import com.instagram.model.Comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    private static final Logger logger =
            LoggerFactory.getLogger(CommentServiceImpl.class);

    private CommentDAO commentDAO;


    public CommentServiceImpl() {

        commentDAO = new CommentDAOImpl();
    }


    // CREATE COMMENT
    @Override
    public boolean createComment(Comment comment) {

        if (comment == null) {

            logger.warn(
                    "Comment creation failed: comment is null"
            );

            return false;
        }


        if (comment.getPost() == null) {

            logger.warn(
                    "Comment creation failed: post is null"
            );

            return false;
        }


        if (comment.getPost().getPost_id() <= 0) {

            logger.warn(
                    "Comment creation failed: invalid post_id={}",
                    comment.getPost().getPost_id()
            );

            return false;
        }


        if (comment.getUser() == null) {

            logger.warn(
                    "Comment creation failed: user is null"
            );

            return false;
        }


        if (comment.getUser().getUser_id() <= 0) {

            logger.warn(
                    "Comment creation failed: invalid user_id={}",
                    comment.getUser().getUser_id()
            );

            return false;
        }


        if (comment.getComment_text() == null ||
                comment.getComment_text().trim().isEmpty()) {

            logger.warn(
                    "Comment creation failed: comment text is empty"
            );

            return false;
        }


        boolean result =
                commentDAO.createComment(comment);


        if (result) {

            logger.info(
                    "Comment creation completed successfully for post_id={}",
                    comment.getPost().getPost_id()
            );
        }

        return result;
    }


    // FIND COMMENTS BY POST ID
    @Override
    public List<Comment> findCommentsByPostId(int post_id) {

        if (post_id <= 0) {

            logger.warn(
                    "Comment search failed: invalid post_id={}",
                    post_id
            );

            return List.of();
        }


        List<Comment> comments =
                commentDAO.findCommentsByPostId(post_id);


        logger.info(
                "Retrieved {} comments for post_id={}",
                comments.size(),
                post_id
        );

        return comments;
    }


    // UPDATE COMMENT
    @Override
    public boolean updateComment(Comment comment) {

        if (comment == null) {

            logger.warn(
                    "Comment update failed: comment is null"
            );

            return false;
        }


        if (comment.getComment_id() <= 0) {

            logger.warn(
                    "Comment update failed: invalid comment_id={}",
                    comment.getComment_id()
            );

            return false;
        }


        if (comment.getUser() == null) {

            logger.warn(
                    "Comment update failed: user is null"
            );

            return false;
        }


        if (comment.getUser().getUser_id() <= 0) {

            logger.warn(
                    "Comment update failed: invalid user_id={}",
                    comment.getUser().getUser_id()
            );

            return false;
        }


        if (comment.getComment_text() == null ||
                comment.getComment_text().trim().isEmpty()) {

            logger.warn(
                    "Comment update failed: comment text is empty"
            );

            return false;
        }


        boolean result =
                commentDAO.updateComment(comment);


        if (result) {

            logger.info(
                    "Comment update completed successfully: comment_id={}",
                    comment.getComment_id()
            );
        }

        return result;
    }


    // DELETE COMMENT
    @Override
    public boolean deleteComment(Comment comment) {

        if (comment == null) {

            logger.warn(
                    "Comment deletion failed: comment is null"
            );

            return false;
        }


        if (comment.getComment_id() <= 0) {

            logger.warn(
                    "Comment deletion failed: invalid comment_id={}",
                    comment.getComment_id()
            );

            return false;
        }


        if (comment.getUser() == null) {

            logger.warn(
                    "Comment deletion failed: user is null"
            );

            return false;
        }


        if (comment.getUser().getUser_id() <= 0) {

            logger.warn(
                    "Comment deletion failed: invalid user_id={}",
                    comment.getUser().getUser_id()
            );

            return false;
        }


        boolean result =
                commentDAO.deleteComment(comment);


        if (result) {

            logger.info(
                    "Comment deletion completed successfully: comment_id={}",
                    comment.getComment_id()
            );
        }

        return result;
    }
}