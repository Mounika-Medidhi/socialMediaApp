package com.instagram.controller;

import com.instagram.model.Profile;
import com.instagram.service.ProfileService;
import com.instagram.service.ProfileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProfileController {

    private static final Logger logger =
            LoggerFactory.getLogger(ProfileController.class);

    private ProfileService profileService;

    public ProfileController() {
        profileService = new ProfileServiceImpl();
    }

    public boolean createProfile(Profile profile) {

        logger.info(
                "Create profile request received: user_id={}",
                profile.getUser().getUser_id()
        );

        return profileService.createProfile(profile);
    }

    public Profile findProfileByUserId(int user_id) {

        logger.info(
                "Find profile request received: user_id={}",
                user_id
        );

        return profileService.findProfileByUserId(user_id);
    }

    public Profile findProfileById(int profile_id) {

        logger.info(
                "Find profile request received: profile_id={}",
                profile_id
        );

        return profileService.findProfileById(profile_id);
    }

    public List<Profile> findAllProfiles() {

        logger.info(
                "Find all profiles request received"
        );

        return profileService.findAllProfiles();
    }

    public boolean Updateprofile(Profile profile) {

        logger.info(
                "Update profile request received: user_id={}",
                profile.getUser().getUser_id()
        );

        return profileService.UpdateProfile(profile);
    }
}