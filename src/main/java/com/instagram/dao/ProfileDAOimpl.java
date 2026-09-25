package com.instagram.dao;

import com.instagram.model.Profile;
import com.instagram.util.JDBCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProfileDAOimpl implements ProfileDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(ProfileDAOimpl.class);

    private static final String CREATE_PROFILE =
            "INSERT INTO profiles " +
                    "(user_id, full_name, bio, phone, profile_image_url) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_PROFILE_BY_USER_ID =
            "SELECT * FROM profiles WHERE user_id = ?";

    private static final String FIND_PROFILE_BY_ID =
            "SELECT * FROM profiles WHERE profile_id = ?";

    private static final String FIND_ALL_PROFILES =
            "SELECT * FROM profiles";

    private static final String UPDATE_PROFILE =
            "UPDATE profiles " +
                    "SET full_name = ?, bio = ?, phone = ?, profile_image_url = ? " +
                    "WHERE user_id = ?";


    @Override
    public boolean createProfile(Profile profile) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(CREATE_PROFILE)) {

            statement.setInt(
                    1,
                    profile.getUser().getUser_id()
            );

            statement.setString(
                    2,
                    profile.getFull_name()
            );

            statement.setString(
                    3,
                    profile.getBio()
            );

            statement.setString(
                    4,
                    profile.getPhone()
            );

            statement.setString(
                    5,
                    profile.getProfile_image_url()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Profile created successfully: user_id={}",
                        profile.getUser().getUser_id()
                );

                return true;
            }

        } catch (Exception e) {

            logger.error(
                    "Error while creating profile: user_id={}",
                    profile.getUser().getUser_id(),
                    e
            );
        }

        return false;
    }


    @Override
    public Profile findProfileByUserId(int user_id) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_PROFILE_BY_USER_ID)) {

            statement.setInt(1, user_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Profile profile = new Profile();

                profile.setProfile_id(
                        resultSet.getInt("profile_id")
                );

                profile.setFull_name(
                        resultSet.getString("full_name")
                );

                profile.setBio(
                        resultSet.getString("bio")
                );

                profile.setPhone(
                        resultSet.getString("phone")
                );

                profile.setProfile_image_url(
                        resultSet.getString("profile_image_url")
                );

                if (resultSet.getTimestamp("updated_at") != null) {
                    profile.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                return profile;
            }

        } catch (Exception e) {

            logger.error(
                    "Error while finding profile: user_id={}",
                    user_id,
                    e
            );
        }

        return null;
    }


    @Override
    public Profile findProfileById(int profile_id) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_PROFILE_BY_ID)) {

            statement.setInt(1, profile_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Profile profile = new Profile();

                profile.setProfile_id(
                        resultSet.getInt("profile_id")
                );

                profile.setFull_name(
                        resultSet.getString("full_name")
                );

                profile.setBio(
                        resultSet.getString("bio")
                );

                profile.setPhone(
                        resultSet.getString("phone")
                );

                profile.setProfile_image_url(
                        resultSet.getString("profile_image_url")
                );

                if (resultSet.getTimestamp("updated_at") != null) {
                    profile.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                return profile;
            }

        } catch (Exception e) {

            logger.error(
                    "Error while finding profile: profile_id={}",
                    profile_id,
                    e
            );
        }

        return null;
    }


    @Override
    public List<Profile> findAllProfiles() {

        List<Profile> profiles = new ArrayList<>();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_PROFILES);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Profile profile = new Profile();

                profile.setProfile_id(
                        resultSet.getInt("profile_id")
                );

                profile.setFull_name(
                        resultSet.getString("full_name")
                );

                profile.setBio(
                        resultSet.getString("bio")
                );

                profile.setPhone(
                        resultSet.getString("phone")
                );

                profile.setProfile_image_url(
                        resultSet.getString("profile_image_url")
                );

                if (resultSet.getTimestamp("updated_at") != null) {
                    profile.setUpdated_at(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                profiles.add(profile);
            }

            logger.info(
                    "Retrieved {} profiles from database",
                    profiles.size()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while retrieving all profiles",
                    e
            );
        }

        return profiles;
    }


    @Override
    public boolean Updateprofile(Profile profile) {

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_PROFILE)) {

            statement.setString(
                    1,
                    profile.getFull_name()
            );

            statement.setString(
                    2,
                    profile.getBio()
            );

            statement.setString(
                    3,
                    profile.getPhone()
            );

            statement.setString(
                    4,
                    profile.getProfile_image_url()
            );

            statement.setInt(
                    5,
                    profile.getUser().getUser_id()
            );

            int rows = statement.executeUpdate();

            if (rows > 0) {

                logger.info(
                        "Profile updated successfully: user_id={}",
                        profile.getUser().getUser_id()
                );

                return true;
            }

            logger.warn(
                    "No profile updated: user_id={}",
                    profile.getUser().getUser_id()
            );

        } catch (Exception e) {

            logger.error(
                    "Error while updating profile: user_id={}",
                    profile.getUser().getUser_id(),
                    e
            );
        }

        return false;
    }
}