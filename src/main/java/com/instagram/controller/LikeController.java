package com.instagram.controller;

import com.instagram.model.Like;
import com.instagram.service.LikeService;
import com.instagram.service.LikeServiceImpl;

import java.util.List;

public class LikeController {

    private LikeService likeService;


    public LikeController() {

        likeService = new LikeServiceImpl();
    }
    // CREATE LIKE
    public boolean createLike(Like like) {

        return likeService.createLike(like);
    }
    // FIND LIKES BY POST ID
    public List<Like> findLikesByPostId(int post_id) {

        return likeService.findLikesByPostId(post_id);
    }
    // DELETE LIKE
    public boolean deleteLike(int user_id, int post_id) {

        return likeService.deleteLike(
                user_id,
                post_id
        );
    }
    // COUNT LIKES BY POST ID
    public int countLikesByPostId(int post_id) {

        return likeService.countLikesByPostId(post_id);
    }
}