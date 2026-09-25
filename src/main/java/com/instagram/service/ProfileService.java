package com.instagram.service;

import com.instagram.model.Profile;

import java.util.List;

public interface ProfileService {

    boolean createProfile(Profile profile);

    Profile findProfileByUserId(int user_id);

    Profile findProfileById(int profile_id);

    List<Profile> findAllProfiles();

    boolean UpdateProfile(Profile profile);
}