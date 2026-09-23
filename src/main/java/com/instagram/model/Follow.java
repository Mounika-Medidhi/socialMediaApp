package com.instagram.model;

import java.time.LocalDateTime;

public class Follow {

    private int follow_id;
    private User follower;
    private User following;
    private LocalDateTime created_at;

    public Follow() {
    }

    public Follow(int follow_id,
                  User follower,
                  User following,
                  LocalDateTime created_at) {

        this.follow_id = follow_id;
        this.follower = follower;
        this.following = following;
        this.created_at = created_at;
    }

    public int getFollow_id() {
        return follow_id;
    }

    public void setFollow_id(int follow_id) {
        this.follow_id = follow_id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}