package com.instagram.dao;

import com.instagram.model.Profile;

import java.util.List;

public interface ProfileDAO {

    boolean createProfile(Profile profile);

    Profile findProfileByUserId(int user_id);

    Profile findProfileById(int profile_id);

    List<Profile> findAllProfiles();

    boolean Updateprofile(Profile profile);
}