package com.instagram.service;

import com.instagram.model.Like;

import java.util.List;

public interface LikeService {

    boolean createLike(Like like);

    List<Like> findLikesByPostId(int post_id);

    boolean deleteLike(int user_id, int post_id);

    int countLikesByPostId(int post_id);
}