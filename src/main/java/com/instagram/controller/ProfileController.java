package com.instagram.controller;

import com.instagram.model.Profile;
import com.instagram.service.ProfileService;
import com.instagram.service.ProfileServiceImpl;

import java.util.List;

public class ProfileController {

    private ProfileService profileService;

    public ProfileController() {
        profileService = new ProfileServiceImpl();
    }

    public boolean createProfile(Profile profile) {

        return profileService.createProfile(profile);
    }

    public Profile findProfileByUserId(int user_id) {

        return profileService.findProfileByUserId(user_id);
    }

    public Profile findProfileById(int profile_id) {

        return profileService.findProfileById(profile_id);
    }

    public List<Profile> findAllProfiles() {

        return profileService.findAllProfiles();
    }

    public boolean updateprofile(Profile profile) {

        return profileService.updateprofile(profile);
    }
}