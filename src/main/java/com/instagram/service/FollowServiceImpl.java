package com.instagram.service;

import com.instagram.dao.FollowDAO;
import com.instagram.dao.FollowDAOImpl;
import com.instagram.model.Follow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FollowServiceImpl implements Followservice {

    private static final Logger logger =
            LoggerFactory.getLogger(FollowServiceImpl.class);

    private FollowDAO followDAO;


    public FollowServiceImpl() {

        followDAO = new FollowDAOImpl();
    }


    // Constructor used for JUnit + Mockito testing
    public FollowServiceImpl(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }


    @Override
    public boolean createFollow(Follow follow) {

        if (follow == null) {

            /*
            logger.warn(
                    "Follow creation failed: follow is null"
            );
            */

            return false;
        }

        if (follow.getFollower() == null) {

            /*
            logger.warn(
                    "Follow creation failed: follower is null"
            );
            */

            return false;
        }

        if (follow.getFollowing() == null) {

            /*
            logger.warn(
                    "Follow creation failed: following user is null"
            );
            */

            return false;
        }

        int follower_id =
                follow.getFollower().getUser_id();

        int following_id =
                follow.getFollowing().getUser_id();

        if (follower_id <= 0) {

            /*
            logger.warn(
                    "Follow creation failed: invalid follower_id={}",
                    follower_id
            );
            */

            return false;
        }

        if (following_id <= 0) {

            /*
            logger.warn(
                    "Follow creation failed: invalid following_id={}",
                    following_id
            );
            */

            return false;
        }

        // Prevent a user from following themselves
        if (follower_id == following_id) {

            logger.warn(
                    "Follow failed: user attempted to follow themselves, user_id={}",
                    follower_id
            );

            throw new IllegalArgumentException(
                    "You cannot follow yourself."
            );
        }

        // Check whether the user is already following the target user
        List<Follow> existingFollowing =
                followDAO.findFollowingByUsername(
                        follow.getFollower().getUsername()
                );

        for (Follow existingFollow : existingFollowing) {

            if (existingFollow.getFollowing() != null
                    && existingFollow.getFollowing().getUser_id()
                    == following_id) {

                logger.warn(
                        "Follow failed: user_id={} is already following user_id={}",
                        follower_id,
                        following_id
                );

                return false;
            }
        }

        boolean result =
                followDAO.createFollow(follow);

        if (result) {

            logger.info(
                    "Follow created successfully: follower_id={}, following_id={}",
                    follower_id,
                    following_id
            );
        }

        return result;
    }


    @Override
    public List<Follow> findFollowersByUsername(String username) {

        if (username == null ||
                username.trim().isEmpty()) {

            /*
            logger.warn(
                    "Finding followers failed: username is empty"
            );
            */

            return List.of();
        }

        List<Follow> follows =
                followDAO.findFollowersByUsername(username);

        /*
        if (follows.isEmpty()) {

            logger.info(
                    "No followers found for username={}",
                    username
            );

            return follows;
        }
        */

        /*
        logger.info(
                "Followers found successfully: username={}, count={}",
                username,
                follows.size()
        );
        */

        return follows;
    }


    @Override
    public List<Follow> findFollowingByUsername(String username) {

        if (username == null ||
                username.trim().isEmpty()) {

            /*
            logger.warn(
                    "Finding following users failed: username is empty"
            );
            */

            return List.of();
        }

        List<Follow> follows =
                followDAO.findFollowingByUsername(username);

        /*
        if (follows.isEmpty()) {

            logger.info(
                    "No following users found for username={}",
                    username
            );

            return follows;
        }
        */

        /*
        logger.info(
                "Following users found successfully: username={}, count={}",
                username,
                follows.size()
        );
        */

        return follows;
    }


    @Override
    public boolean deleteFollow(
            int follower_id,
            int following_id) {

        if (follower_id <= 0) {

            /*
            logger.warn(
                    "Unfollow failed: invalid follower_id={}",
                    follower_id
            );
            */

            return false;
        }

        if (following_id <= 0) {

            /*
            logger.warn(
                    "Unfollow failed: invalid following_id={}",
                    following_id
            );
            */

            return false;
        }

        // Prevent a user from unfollowing themselves
        if (follower_id == following_id) {

            logger.warn(
                    "Unfollow failed: user attempted to unfollow themselves, user_id={}",
                    follower_id
            );

            throw new IllegalArgumentException(
                    "You cannot unfollow yourself."
            );
        }

        boolean result =
                followDAO.deleteFollow(
                        follower_id,
                        following_id
                );

        if (result) {

            logger.info(
                    "Unfollow completed successfully: follower_id={}, following_id={}",
                    follower_id,
                    following_id
            );
        }

        return result;
    }
}