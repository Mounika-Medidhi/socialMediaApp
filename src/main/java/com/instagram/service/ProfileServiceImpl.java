package com.instagram.service;

import com.instagram.dao.ProfileDAO;
import com.instagram.dao.ProfileDAOimpl;
import com.instagram.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger =
            LoggerFactory.getLogger(ProfileServiceImpl.class);

    private ProfileDAO profileDAO;

    public ProfileServiceImpl() {
        profileDAO = new ProfileDAOimpl();
    }

    // Constructor used for JUnit + Mockito testing
    public ProfileServiceImpl(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Override
    public boolean createProfile(Profile profile) {

        if (profile == null) {

            logger.warn(
                    "Profile creation failed: profile is null"
            );

            return false;
        }

        if (profile.getUser() == null) {

            logger.warn(
                    "Profile creation failed: user is null"
            );

            return false;
        }

        if (profile.getUser().getUser_id() <= 0) {

            logger.warn(
                    "Profile creation failed: invalid user_id={}",
                    profile.getUser().getUser_id()
            );

            return false;
        }

        if (profile.getFull_name() == null ||
                profile.getFull_name().trim().isEmpty()) {

            logger.warn(
                    "Profile creation failed: full name is empty"
            );

            return false;
        }

        if (profile.getBio() == null ||
                profile.getBio().trim().isEmpty()) {

            logger.warn(
                    "Profile creation failed: bio is empty"
            );

            return false;
        }

        if (profile.getPhone() == null ||
                profile.getPhone().trim().isEmpty()) {

            logger.warn(
                    "Profile creation failed: phone number is empty"
            );

            return false;
        }

        if (profile.getProfile_image_url() == null ||
                profile.getProfile_image_url().trim().isEmpty()) {

            logger.warn(
                    "Profile creation failed: profile image URL is empty"
            );

            return false;
        }

        return profileDAO.createProfile(profile);
    }

    @Override
    public Profile findProfileByUserId(int user_id) {

        if (user_id <= 0) {

            logger.warn(
                    "Profile search failed: invalid user_id={}",
                    user_id
            );

            return null;
        }

        Profile profile =
                profileDAO.findProfileByUserId(user_id);

        if (profile == null) {

            logger.warn(
                    "Profile not found for user_id={}",
                    user_id
            );

            return null;
        }

        return profile;
    }

    @Override
    public Profile findProfileById(int profile_id) {

        if (profile_id <= 0) {

            logger.warn(
                    "Profile search failed: invalid profile_id={}",
                    profile_id
            );

            return null;
        }

        Profile profile =
                profileDAO.findProfileById(profile_id);

        if (profile == null) {

            logger.warn(
                    "Profile not found for profile_id={}",
                    profile_id
            );

            return null;
        }

        logger.info(
                "Profile found successfully for profile_id={}",
                profile_id
        );

        return profile;
    }

    @Override
    public List<Profile> findAllProfiles() {

        List<Profile> profiles =
                profileDAO.findAllProfiles();

        if (profiles.isEmpty()) {

            logger.warn(
                    "No profiles found"
            );

            return profiles;
        }

        logger.info(
                "Retrieved {} profiles successfully",
                profiles.size()
        );

        return profiles;
    }

    @Override
    public boolean updateprofile(Profile profile) {

        if (profile == null) {

            logger.warn(
                    "Profile update failed: profile is null"
            );

            return false;
        }

        if (profile.getUser() == null) {

            logger.warn(
                    "Profile update failed: user is null"
            );

            return false;
        }

        if (profile.getUser().getUser_id() <= 0) {

            logger.warn(
                    "Profile update failed: invalid user_id={}",
                    profile.getUser().getUser_id()
            );

            return false;
        }

        if (profile.getFull_name() == null ||
                profile.getFull_name().trim().isEmpty()) {

            logger.warn(
                    "Profile update failed: full name is empty"
            );

            return false;
        }

        if (profile.getBio() == null ||
                profile.getBio().trim().isEmpty()) {

            logger.warn(
                    "Profile update failed: bio is empty"
            );

            return false;
        }

        if (profile.getPhone() == null ||
                profile.getPhone().trim().isEmpty()) {

            logger.warn(
                    "Profile update failed: phone number is empty"
            );

            return false;
        }

        if (profile.getProfile_image_url() == null ||
                profile.getProfile_image_url().trim().isEmpty()) {

            logger.warn(
                    "Profile update failed: profile image URL is empty"
            );

            return false;
        }

        return profileDAO.updateprofile(profile);
    }
}
