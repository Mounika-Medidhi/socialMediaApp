package com.instagram.service;

import com.instagram.dao.LikeDAO;
import com.instagram.dao.LikeDAOimpl;
import com.instagram.dao.PostDAO;
import com.instagram.dao.PostDAOImpl;
import com.instagram.model.Like;
import com.instagram.model.Post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LikeServiceImpl implements LikeService {

    private static final Logger logger =
            LoggerFactory.getLogger(LikeServiceImpl.class);

    private LikeDAO likeDAO;
    private PostDAO postDAO;


    public LikeServiceImpl() {

        likeDAO = new LikeDAOimpl();
        postDAO = new PostDAOImpl();
    }


    // Constructor used for JUnit + Mockito testing
    public LikeServiceImpl(LikeDAO likeDAO) {

        this.likeDAO = likeDAO;
        this.postDAO = new PostDAOImpl();
    }


    // Constructor used for JUnit + Mockito testing
    public LikeServiceImpl(LikeDAO likeDAO, PostDAO postDAO) {

        this.likeDAO = likeDAO;
        this.postDAO = postDAO;
    }


    @Override
    public boolean createLike(Like like) {

        if (like == null) {
            return false;
        }

        if (like.getUser() == null) {
            return false;
        }

        if (like.getPost() == null) {
            return false;
        }

        if (like.getUser().getUser_id() <= 0) {
            return false;
        }

        if (like.getPost().getPost_id() <= 0) {
            return false;
        }


        List<Post> posts =
                postDAO.findAllPosts();

        for (Post post : posts) {

            if (post.getPost_id() ==
                    like.getPost().getPost_id()) {

                if (post.getUser() != null &&
                        post.getUser().getUser_id() ==
                                like.getUser().getUser_id()) {

                    logger.warn(
                            "Like rejected: user cannot like their own post"
                    );

                    return false;
                }

                break;
            }
        }


        return likeDAO.createLike(like);
    }


    @Override
    public List<Like> findLikesByPostId(int post_id) {

        if (post_id <= 0) {
            return List.of();
        }

        List<Like> likes =
                likeDAO.findLikesByPostId(post_id);

        return likes;
    }


    @Override
    public boolean deleteLike(int user_id, int post_id) {

        if (user_id <= 0) {
            return false;
        }

        if (post_id <= 0) {
            return false;
        }

        return likeDAO.deleteLike(
                user_id,
                post_id
        );
    }


    @Override
    public int countLikesByPostId(int post_id) {

        if (post_id <= 0) {
            return 0;
        }

        return likeDAO.countLikesByPostId(post_id);
    }
}