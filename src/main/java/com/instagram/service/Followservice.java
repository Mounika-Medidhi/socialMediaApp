package com.instagram.service;

import com.instagram.model.Follow;

import java.util.List;

public interface Followservice {

    boolean createFollow(Follow follow);

    List<Follow> findFollowersByUsername(String username);

    List<Follow> findFollowingByUsername(String username);
    boolean deleteFollow(int follower_id, int following_id);
}