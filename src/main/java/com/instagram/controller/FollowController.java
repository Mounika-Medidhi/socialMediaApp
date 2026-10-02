package com.instagram.controller;

import com.instagram.model.Follow;
import com.instagram.service.Followservice;
import com.instagram.service.FollowServiceImpl;

import java.util.List;

public class FollowController {

    private Followservice followService;


    public FollowController() {

        followService = new FollowServiceImpl();
    }


    // CREATE FOLLOW
    public boolean createFollow(Follow follow) {

        return followService.createFollow(follow);
    }


    // FIND FOLLOWERS BY USERNAME
    public List<Follow> findFollowersByUsername(String username) {

        return followService.findFollowersByUsername(username);
    }


    // FIND FOLLOWING BY USERNAME
    public List<Follow> findFollowingByUsername(String username) {

        return followService.findFollowingByUsername(username);
    }


    // DELETE FOLLOW
    public boolean deleteFollow(
            int follower_id,
            int following_id) {

        return followService.deleteFollow(
                follower_id,
                following_id
        );
    }
}