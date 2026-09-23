package com.instagram.model;

import java.time.LocalDateTime;

public class Profile {

    private int profile_id;
    private User user;
    private String full_name;
    private String bio;
    private String phone;
    private String profile_image_url;
    private LocalDateTime updated_at;

    public Profile() {
    }

    public Profile(int profile_id,
                   User user,
                   String full_name,
                   String bio,
                   String phone,
                   String profile_image_url,
                   LocalDateTime updated_at) {

        this.profile_id = profile_id;
        this.user = user;
        this.full_name = full_name;
        this.bio = bio;
        this.phone = phone;
        this.profile_image_url = profile_image_url;
        this.updated_at = updated_at;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}