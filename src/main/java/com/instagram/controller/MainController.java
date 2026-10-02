package com.instagram.controller;
import com.instagram.exception.InvalidPasswordException;
import com.instagram.exception.LoginException;
import com.instagram.exception.UserNotFoundException;

import com.instagram.model.Profile;
import com.instagram.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.instagram.model.Post;
import com.instagram.model.Like;
import com.instagram.model.Follow;
import com.instagram.model.Comment;
import java.util.List;
import java.util.Collections;

import java.util.Map;
import java.util.Scanner;

public class MainController {
    private static final Logger logger =
            LoggerFactory.getLogger(MainController.class);
    // SAFE MENU INPUt
    private static String readRequiredInput(
            Scanner scanner,
            String prompt,
            String errorMessage) {

        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine();

            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
            System.out.println(errorMessage);
        }
    }
    private static int getMenuChoice(Scanner scanner) {

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            System.out.print("Enter your choice: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Creating controller objects
        UserController userController = new UserController();
        ProfileController profileController = new ProfileController();
        PostController postController = new PostController();
        LikeController likeController = new LikeController();
        CommentController commentController = new CommentController();
        FollowController followController = new FollowController();
        boolean applicationRunning = true;
        while (applicationRunning) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("     SOCIAL MEDIA APPLICATION");
            System.out.println("=================================");

            System.out.println("1. User");
            System.out.println("2. Admin");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = getMenuChoice(scanner);
            switch (choice) {
                case 1:
                    userMenu(
                            scanner,
                            userController,
                            profileController,
                            postController,
                            likeController,
                            commentController,
                            followController
                    );
                    break;
                case 2:
                    adminLogin(scanner, userController);
                    break;
                case 3:

                    System.out.println("Application exited.");

                    applicationRunning = false;

                    break;

                default:

                    logger.warn("Invalid main menu choice: {}", choice);

                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
    // USER MENU
    private static void userMenu(
            Scanner scanner,
            UserController userController,
            ProfileController profileController,
            PostController postController,
            LikeController likeController,
            CommentController commentController,
            FollowController followController) {

        System.out.println();
        System.out.println("========== USER MENU ==========");
        System.out.println("1. Signup User");
        System.out.println("2. Login User");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int userChoice = getMenuChoice(scanner);

        switch (userChoice) {
            case 1:
                signupUser(scanner, userController, profileController);
                break;
            case 2:
                loginUser(scanner, userController, profileController, postController, likeController, commentController, followController);
                break;
            case 3:
                System.out.println("Exiting User Menu.");
                break;
            default:
                logger.warn("Invalid user menu choice: {}", userChoice);
                System.out.println("Invalid choice. Please try again.");
        }
    }
    // USER SIGNUP
    private static void signupUser(
            Scanner scanner,
            UserController userController,
            ProfileController profileController) {

        System.out.println();
        System.out.println("========== USER SIGNUP ==========");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword_hash(password);

        boolean signupResult;

        try {
            signupResult = userController.signupUser(user);

        } catch (InvalidPasswordException e) {
            System.out.println("Invalid password.");
            System.out.println("Please enter a strong password.");
            System.out.println("Password must contain:");
            System.out.println("- At least 8 characters");
            System.out.println("- One uppercase letter");
            System.out.println("- One lowercase letter");
            System.out.println("- One digit");
            System.out.println("- One special character");
            return;
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        catch (Exception e) {
            logger.error("Error occurred during user signup: username={}", username, e);
            System.out.println("Signup failed due to an unexpected error.");
            return;
        }
        if (!signupResult) {
            System.out.println("Username or email already exists.");
            return;
        }
        System.out.println("Signup successful!");
        User createdUser;
        try {
            createdUser = userController.searchUserByUsername(username);
        } catch (Exception e) {
            logger.error("Error retrieving newly created user: username={}", username, e);
            System.out.println("Unable to retrieve your account details.");
            return;
        }
        if (createdUser == null) {
            System.out.println("Unable to retrieve your account details.");
            return;
        }

        System.out.println();
        System.out.println("========== CREATE PROFILE ==========");

        String fullName = readRequiredInput(
                scanner,
                "Enter full name: ",
                "Full name cannot be empty."
        );

        String bio = readRequiredInput(
                scanner,
                "Enter bio: ",
                "Bio cannot be empty."
        );

        String phone = readRequiredInput(
                scanner,
                "Enter phone: ",
                "Phone number cannot be empty."
        );

        String profileImageUrl = readRequiredInput(
                scanner,
                "Enter profile image URL: ",
                "Profile image URL cannot be empty."
        );

        Profile profile = new Profile();

        profile.setUser(createdUser);
        profile.setFull_name(fullName);
        profile.setBio(bio);
        profile.setPhone(phone);
        profile.setProfile_image_url(profileImageUrl);

        // Check phone number before attempting profile creation.
        // This gives the user a clear message instead of a database error.
        if (isPhoneAlreadyUsed(phone, profileController)) {
            System.out.println("Phone number already exists. Please use a different phone number.");
            return;
        }

        boolean profileResult;

        try {
            profileResult = profileController.createProfile(profile);
        } catch (Exception e) {

            logger.error("Error occurred while creating profile: user_id={}", createdUser.getUser_id(), e);

            System.out.println("Profile creation failed due to an unexpected error.");

            return;
        }
        if (!profileResult) {
            System.out.println("Profile creation failed. Please try again.");
            return;
        }
        System.out.println("Profile created successfully!");

        Profile createdProfile;
        try {
            createdProfile = profileController.findProfileByUserId(createdUser.getUser_id());
        } catch (Exception e) {

            logger.error("Error retrieving profile: user_id={}", createdUser.getUser_id(), e);

            System.out.println("Unable to retrieve your profile details.");

            return;
        }

        if (createdProfile == null) {

            System.out.println("Unable to retrieve your profile details.");

            return;
        }

        System.out.println("Your Profile ID: " + createdProfile.getProfile_id());
    }
// CREATE PROFILE AFTER LOGIN

    private static Profile createProfile(
            Scanner scanner,
            User loggedInUser,
            ProfileController profileController) {

        System.out.println();
        System.out.println("========== CREATE PROFILE ==========");

        Profile existingProfile;
        try {
            existingProfile = profileController.findProfileByUserId(loggedInUser.getUser_id());
        } catch (Exception e) {
            logger.error("Error checking profile: user_id={}", loggedInUser.getUser_id(), e);
            System.out.println("Unable to check existing profile.");
            return null;
        }

        if (existingProfile != null) {
            System.out.println("Profile already exists for this user.");
            return existingProfile;
        }

        String fullName = readRequiredInput(scanner, "Enter full name: ", "Full name cannot be empty.");
        String bio = readRequiredInput(scanner, "Enter bio: ", "Bio cannot be empty.");
        String phone = readRequiredInput(scanner, "Enter phone: ", "Phone number cannot be empty.");
        String profileImageUrl = readRequiredInput(scanner, "Enter profile image URL: ", "Profile image URL cannot be empty.");

        Profile profile = new Profile();
        profile.setUser(loggedInUser);
        profile.setFull_name(fullName);
        profile.setBio(bio);
        profile.setPhone(phone);
        profile.setProfile_image_url(profileImageUrl);

        if (isPhoneAlreadyUsed(phone, profileController)) {
            System.out.println("Phone number already exists. Please use a different phone number.");
            return null;
        }

        try {
            boolean result = profileController.createProfile(profile);
            if (result) {
                System.out.println("Profile created successfully!");
                return profileController.findProfileByUserId(loggedInUser.getUser_id());
            } else {
                System.out.println("Profile creation failed.");
            }
        } catch (Exception e) {
            logger.error("Error creating profile: user_id={}", loggedInUser.getUser_id(), e);
            System.out.println("Profile creation failed due to an unexpected error.");
        }

        return null;
    }
    // USER LOGIN
    private static void loginUser(
            Scanner scanner,
            UserController userController,
            ProfileController profileController,
            PostController postController,
            LikeController likeController,
            CommentController commentController,
            FollowController followController) {

        System.out.println();
        System.out.println("========== USER LOGIN ==========");

        String username = readRequiredInput(
                scanner,
                "Enter username: ",
                "Username cannot be empty."
        );

        String password = readRequiredInput(
                scanner,
                "Enter password: ",
                "Password cannot be empty."
        );
        try {
            // FIND USER
            User loggedInUser;
            try {
                loggedInUser = userController.searchUserByUsername(username);
            } catch (IllegalArgumentException e) {
                throw new UserNotFoundException(
                        "No such user exists."
                );
            }
            // CHECK PASSWORD
            if (!loggedInUser.getPassword_hash().equals(password)) {

                throw new InvalidPasswordException(
                        "Invalid password."
                );
            }
            // CHECK PROFILE
            Profile profile;

            try {

                profile =
                        profileController.findProfileByUserId(
                                loggedInUser.getUser_id()
                        );

            } catch (Exception e) {

                throw new LoginException(
                        "Login failed.",
                        e
                );
            }
            // LOGIN SUCCESS
            System.out.println("Login successful!");
            // OPEN USER OPERATions
            userOperationsMenu(
                    scanner,
                    loggedInUser,
                    profile,
                    profileController,
                    userController,
                    postController,
                    likeController,
                    commentController,
                    followController
            );


        } catch (UserNotFoundException e) {

            System.out.println(e.getMessage());

        } catch (InvalidPasswordException e) {

            System.out.println(e.getMessage());

        } catch (LoginException e) {

            System.out.println(e.getMessage());
        }
    }
// =====================================================
    // USER OPERATIONS MENU
    // =====================================================

    private static void userOperationsMenu(
            Scanner scanner,
            User loggedInUser,
            Profile profile,
            ProfileController profileController,
            UserController userController,
            PostController postController,
            LikeController likeController,
            CommentController commentController,
            FollowController followController) {

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println();
            System.out.println("==================================================");
            System.out.println("              USER OPERATIONS MENU");
            System.out.println("==================================================");
            System.out.println("PROFILE");
            System.out.println("--------------------------------------------------");
            System.out.println("1. Create Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Profile");
            System.out.println("4. Search Profile");
            System.out.println("POSTS");
            System.out.println("--------------------------------------------------");
            System.out.println("5. Create Post");
            System.out.println("6. Update Post");
            System.out.println("7. Delete Post");
            System.out.println("8. View All Posts");
            System.out.println("ENGAGEMENT");
            System.out.println("--------------------------------------------------");
            System.out.println("9. Like a Post");
            System.out.println("10. Unlike a Post");
            System.out.println("COMMENTS");
            System.out.println("--------------------------------------------------");
            System.out.println("11. Comment on a Post");
            System.out.println("12. Delete a Comment");
            System.out.println("FOLLOW");
            System.out.println("--------------------------------------------------");
            System.out.println("13. Follow a User");
            System.out.println("14. Unfollow a User");
            System.out.println("15. View Followers");
            System.out.println("16. View Following");
            System.out.println("ACCOUNT");
            System.out.println("--------------------------------------------------");
            System.out.println("17. Logout");
            System.out.println("18. Update User (Email & Password)");
            System.out.println("19. Delete Account");
            System.out.println("==================================================");
            System.out.print("Enter your choice: ");

            int choice = getMenuChoice(scanner);

            switch (choice) {
                case 1:
                    if (profile != null) {
                        System.out.println("Profile already exists for this user.");
                    } else {
                        profile = createProfile(scanner, loggedInUser, profileController);
                    }
                    break;
                case 2:
                    if (profile == null) {
                        System.out.println("Profile not found. Please create your profile first.");
                    } else {
                        updateProfile(scanner, loggedInUser, profile, profileController);
                    }
                    break;
                case 3:
                    viewProfile(loggedInUser, profileController, postController, likeController, commentController, followController);
                    break;
                case 4:
                    searchProfile(scanner, userController, profileController, postController);
                    break;
                case 5:
                    createPost(scanner, loggedInUser, postController);
                    break;
                case 6:
                    updatePost(scanner, loggedInUser, postController);
                    break;
                case 7:
                    deletePost(scanner, loggedInUser, postController);
                    break;
                case 8:
                    viewAllPosts(
                            postController,
                            likeController,
                            commentController
                    );
                    break;
                case 9:
                    likePost(scanner, loggedInUser, postController, likeController);
                    break;
                case 10:
                    unlikePost(scanner, loggedInUser, postController, likeController);
                    break;
                case 11:
                    commentOnPost(scanner, loggedInUser, postController, commentController);
                    break;
                case 12:
                    deleteComment(scanner, loggedInUser, postController, commentController);
                    break;
                case 13:
                    followUser(scanner, loggedInUser, userController, followController);
                    break;
                case 14:
                    unfollowUser(scanner, loggedInUser, userController, followController);
                    break;
                case 15:
                    viewFollowers(loggedInUser, followController);
                    break;
                case 16:
                    viewFollowing(loggedInUser, followController);
                    break;
                case 17:
                    System.out.println("Logout successful!");
                    loggedIn = false;
                    break;
                case 18:
                    updateUserDetails(scanner, loggedInUser, userController);
                    break;
                case 19:
                    boolean accountDeleted = deleteAccount(scanner, loggedInUser, userController);
                    if (accountDeleted) {
                        loggedIn = false;
                    }
                    break;
                default:
                    logger.warn("Invalid user operation choice: {}", choice);
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    // 1. UPDATE PROFILE
    private static void updateProfile(
            Scanner scanner,
            User loggedInUser,
            Profile profile,
            ProfileController profileController) {

        System.out.println();
        System.out.println("========== UPDATE PROFILE ==========");

        String fullName = readRequiredInput(
                scanner,
                "Enter full name: ",
                "Full name cannot be empty."
        );

        String bio = readRequiredInput(
                scanner,
                "Enter bio: ",
                "Bio cannot be empty."
        );

        String phone = readRequiredInput(
                scanner,
                "Enter phone: ",
                "Phone number cannot be empty."
        );

        String profileImageUrl = readRequiredInput(
                scanner,
                "Enter profile image URL: ",
                "Profile image URL cannot be empty."
        );

        profile.setUser(loggedInUser);
        profile.setFull_name(fullName);
        profile.setBio(bio);
        profile.setPhone(phone);
        profile.setProfile_image_url(profileImageUrl);

        boolean result;

        try {

            result =
                    profileController.updateprofile(profile);

        } catch (Exception e) {

            logger.error("Error updating profile: user_id={}", loggedInUser.getUser_id(), e);

            System.out.println("Profile update failed due to an unexpected error.");

            return;
        }

        if (result) {

            System.out.println("Profile updated successfully!");

        } else {

            System.out.println("Profile update failed. Please try again.");
        }
    }
    // UPDATE USER - EMAIL AND PASSWORD
    private static void updateUserDetails(
            Scanner scanner,
            User loggedInUser,
            UserController userController) {
        System.out.println();
        System.out.println("========== UPDATE USER ==========");
        // ==========================================
        // VERIFY CURRENT PASSWORD
        // ==========================================
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!loggedInUser.getPassword_hash().equals(currentPassword)) {

            System.out.println("Invalid current password. Update cancelled.");

            return;
        }
        // ==========================================
        // ENTER NEW DETAILS
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        User updatedUser = new User();
        updatedUser.setUser_id(loggedInUser.getUser_id());
        updatedUser.setUsername(loggedInUser.getUsername());
        updatedUser.setEmail(newEmail);
        updatedUser.setPassword_hash(newPassword);
        boolean result;
        try {
            result = userController.updateUser(updatedUser);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("Error updating user: user_id={}", loggedInUser.getUser_id(), e);
            System.out.println("User update failed due to an unexpected error.");
            return;
        }
        if (result) {

            loggedInUser.setEmail(newEmail);
            loggedInUser.setPassword_hash(newPassword);

            System.out.println("User details updated successfully!");

        } else {

            System.out.println("User update failed. Please try again.");
        }
    }
    // =====================================================
    // DELETE ACCOUNT
    // =====================================================

    private static boolean deleteAccount(
            Scanner scanner,
            User loggedInUser,
            UserController userController) {

        System.out.println();
        System.out.println("========== DELETE ACCOUNT ==========");

        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!loggedInUser.getPassword_hash().equals(currentPassword)) {
            System.out.println("Invalid current password. Account deletion cancelled.");
            return false;
        }
        System.out.print("Are you sure you want to permanently delete your account? (Y/N): ");

        String confirmation = scanner.nextLine();

        if (!"Y".equalsIgnoreCase(confirmation)) {
            System.out.println("Account deletion cancelled.");
            return false;
        }
        boolean result;

        try {
            result = userController.deleteUser(
                    loggedInUser.getUsername(),
                    currentPassword
            );

        } catch (Exception e) {

            logger.error("Error deleting account: user_id={}", loggedInUser.getUser_id(), e);

            System.out.println("Account deletion failed due to an unexpected error.");

            return false;
        }

        if (result) {

            System.out.println("Account deleted successfully.");

            System.out.println("All data associated with your account has been deleted.");

            System.out.println("You have been logged out.");

            return true;
        }

        System.out.println("Account deletion failed. Please try again.");

        return false;
    }
    // =====================================================
    // 2. VIEW PROFILE
    // =====================================================

    private static void viewProfile(
            User loggedInUser,
            ProfileController profileController,
            PostController postController,
            LikeController likeController,
            CommentController commentController,
            FollowController followController) {

        System.out.println();
        System.out.println("==================================================");
        System.out.println("                 MY PROFILE");
        System.out.println("==================================================");

        Profile profile;

        try {
            profile = profileController.findProfileByUserId(
                    loggedInUser.getUser_id()
            );
        } catch (Exception e) {
            logger.error("Error retrieving profile: user_id={}", loggedInUser.getUser_id(), e);
            System.out.println("Unable to retrieve profile.");
            return;
        }

        if (profile == null) {
            System.out.println("Profile not found.");
            return;
        }

        System.out.println("Username       : " + loggedInUser.getUsername());
        System.out.println("Email          : " + loggedInUser.getEmail());
        System.out.println("Full Name      : " + profile.getFull_name());
        System.out.println("Bio            : " + profile.getBio());
        System.out.println("Phone          : " + profile.getPhone());
        System.out.println("Profile Image  : " + profile.getProfile_image_url());

        System.out.println();
        System.out.println("------------------ MY POSTS ------------------");

        List<Post> posts;

        try {
            posts = postController.findPostsByUsername(
                    loggedInUser.getUsername()
            );
        } catch (Exception e) {
            logger.error("Error retrieving own profile posts: username={}", loggedInUser.getUsername(), e);
            System.out.println("Unable to retrieve posts.");
            return;
        }

        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts available.");
        } else {

            for (Post post : posts) {

                if (post == null) {
                    continue;
                }

                System.out.println();
                System.out.println("Post ID        : " + post.getPost_id());
                System.out.println("Caption        : " + post.getCaption());
                System.out.println("Image          : " + post.getImage_url());

                List<Like> likes;

                try {
                    likes = likeController.findLikesByPostId(
                            post.getPost_id()
                    );
                } catch (Exception e) {
                    logger.error("Error retrieving likes: post_id={}", post.getPost_id(), e);
                    likes = Collections.emptyList();
                }

                System.out.println("Liked By:");

                if (likes == null || likes.isEmpty()) {
                    System.out.println("  No likes yet.");
                } else {
                    for (Like like : likes) {
                        if (like == null || like.getUser() == null) {
                            continue;
                        }

                        String likerName = like.getUser().getUsername();

                        if (likerName == null || likerName.trim().isEmpty()) {
                            likerName = "User ID " + like.getUser().getUser_id();
                        }

                        System.out.println("  " + likerName);
                    }

                    System.out.println("Total Likes    : " + likes.size());
                }

                List<Comment> comments;

                try {
                    comments = commentController.findCommentsByPostId(
                            post.getPost_id()
                    );
                } catch (Exception e) {
                    logger.error("Error retrieving comments: post_id={}", post.getPost_id(), e);
                    comments = Collections.emptyList();
                }

                System.out.println("Comments:");

                if (comments == null || comments.isEmpty()) {
                    System.out.println("  No comments yet.");
                } else {
                    for (Comment comment : comments) {
                        if (comment == null) {
                            continue;
                        }

                        String commenter = "Unknown User";

                        if (comment.getUser() != null
                                && comment.getUser().getUsername() != null
                                && !comment.getUser().getUsername().trim().isEmpty()) {
                            commenter = comment.getUser().getUsername();
                        }

                        System.out.println("  " + commenter +" : " + comment.getComment_text());
                    }

                    System.out.println("Total Comments : " + comments.size());
                }

                System.out.println("--------------------------------------------------");
            }
        }

        List<Follow> followers;
        List<Follow> following;

        try {
            followers = followController.findFollowersByUsername(
                    loggedInUser.getUsername()
            );
        } catch (Exception e) {
            logger.error("Error retrieving followers: username={}", loggedInUser.getUsername(), e);
            followers = Collections.emptyList();
        }

        System.out.println();
        System.out.println("------------------ FOLLOWERS ------------------");

        if (followers == null || followers.isEmpty()) {
            System.out.println("No followers yet.");
        } else {
            for (Follow follow : followers) {
                if (follow != null && follow.getFollower() != null) {
                    System.out.println("  " + follow.getFollower().getUsername());
                }
            }
            System.out.println("Total Followers : " + followers.size());
        }

        try {
            following = followController.findFollowingByUsername(
                    loggedInUser.getUsername()
            );
        } catch (Exception e) {
            logger.error("Error retrieving following users: username={}", loggedInUser.getUsername(), e);
            following = Collections.emptyList();
        }

        System.out.println();
        System.out.println("------------------ FOLLOWING ------------------");

        if (following == null || following.isEmpty()) {
            System.out.println("Not following anyone yet.");
        } else {
            for (Follow follow : following) {
                if (follow != null && follow.getFollowing() != null) {
                    System.out.println("  " + follow.getFollowing().getUsername());
                }
            }
            System.out.println("Total Following : " + following.size());
        }

        System.out.println();
        System.out.println("==================================================");
    }


// =====================================================
// POST OPERATIONS
// =====================================================

    // =====================================================
    // VIEW ALL POSTS
    // =====================================================

    private static void viewAllPosts(
            PostController postController,
            LikeController likeController,
            CommentController commentController) {

        System.out.println();
        System.out.println("==================================================");
        System.out.println("                  ALL POSTS");
        System.out.println("==================================================");

        List<Post> posts;

        try {
            posts = postController.findAllPosts();
        } catch (Exception e) {
            logger.error("Error retrieving all posts.", e);
            System.out.println("Unable to retrieve posts.");
            return;
        }

        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts available.");
            return;
        }

        for (Post post : posts) {

            if (post == null) {
                continue;
            }

            System.out.println();
            System.out.println("Post ID       : " + post.getPost_id());

            if (post.getUser() != null) {
                System.out.println("Posted By     : " + post.getUser().getUsername());
            }

            System.out.println("Caption       : " + post.getCaption());
            System.out.println("Image         : " + post.getImage_url());

            int likeCount;

            try {
                likeCount = likeController.countLikesByPostId(post.getPost_id());
            } catch (Exception e) {
                logger.error(
                        "Error retrieving like count: post_id={}",
                        post.getPost_id(),
                        e
                );
                likeCount = 0;
            }

            System.out.println("Likes         : " + likeCount);

            List<Comment> comments;

            try {
                comments = commentController.findCommentsByPostId(post.getPost_id());
            } catch (Exception e) {
                logger.error(
                        "Error retrieving comments: post_id={}",
                        post.getPost_id(),
                        e
                );
                comments = Collections.emptyList();
            }

            System.out.println("Comments:");

            if (comments == null || comments.isEmpty()) {

                System.out.println("  No comments yet.");

            } else {

                for (Comment comment : comments) {

                    if (comment == null) {
                        continue;
                    }

                    String commenter = "Unknown User";

                    if (comment.getUser() != null &&
                            comment.getUser().getUsername() != null) {
                        commenter = comment.getUser().getUsername();
                    }

                    System.out.println(
                            "  " + commenter + " : " + comment.getComment_text()
                    );
                }

                System.out.println("Total Comments : " + comments.size());
            }

            System.out.println("--------------------------------------------------");
        }

        System.out.println("==================================================");
    }

    // =====================================================
    // CREATE POST
    // =====================================================

    private static void createPost(
            Scanner scanner,
            User loggedInUser,
            PostController postController) {

        System.out.println();
        System.out.println("========== CREATE POST ==========");

        System.out.print("Enter caption: ");
        String caption = scanner.nextLine();

        System.out.print("Enter image URL: ");
        String imageUrl = scanner.nextLine();

        Post post = new Post();
        post.setUser(loggedInUser);
        post.setCaption(caption);
        post.setImage_url(imageUrl);

        try {
            boolean result = postController.createPost(post);

            if (result) {
                System.out.println("Post created successfully!");
            } else {
                System.out.println("Post creation failed. Please provide a caption or image.");
            }
        } catch (Exception e) {
            logger.error("Error creating post: user_id={}", loggedInUser.getUser_id(), e);
            System.out.println("Post creation failed due to an unexpected error.");
        }
    }


    private static void updatePost(
            Scanner scanner,
            User loggedInUser,
            PostController postController) {

        System.out.println();
        System.out.println("========== UPDATE POST ==========");

        List<Post> posts;

        try {
            posts = postController.findPostsByUsername(
                    loggedInUser.getUsername()
            );
        } catch (Exception e) {
            logger.error("Error retrieving posts for update: username={}", loggedInUser.getUsername(), e);
            System.out.println("Unable to retrieve your posts.");
            return;
        }

        if (posts == null || posts.isEmpty()) {
            System.out.println("You have no posts to update.");
            return;
        }

        System.out.println("========== YOUR POSTS ==========");
        displayPosts(posts);

        System.out.print("Enter the Post ID to update: ");
        int postId = getMenuChoice(scanner);

        boolean postBelongsToUser = false;
        for (Post post : posts) {
            if (post.getPost_id() == postId) {
                postBelongsToUser = true;
                break;
            }
        }

        if (!postBelongsToUser) {
            System.out.println("Invalid Post ID. Please select one of your posts.");
            return;
        }

        System.out.print("Enter new caption: ");
        String caption = scanner.nextLine();

        System.out.print("Enter new image URL: ");
        String imageUrl = scanner.nextLine();

        Post post = new Post();
        post.setPost_id(postId);
        post.setUser(loggedInUser);
        post.setCaption(caption);
        post.setImage_url(imageUrl);

        try {
            boolean result = postController.updatePost(post);

            if (result) {
                System.out.println("Post updated successfully!");
            } else {
                System.out.println("Post update failed. Please try again.");
            }
        } catch (Exception e) {
            logger.error("Error updating post: post_id={}, username={}", postId, loggedInUser.getUsername(), e);
            System.out.println("Post update failed due to an unexpected error.");
        }
    }


    private static void deletePost(
            Scanner scanner,
            User loggedInUser,
            PostController postController) {

        System.out.println();
        System.out.println("========== DELETE POST ==========");

        List<Post> posts;

        try {
            posts = postController.findPostsByUsername(
                    loggedInUser.getUsername()
            );
        } catch (Exception e) {
            logger.error("Error retrieving posts for deletion: username={}", loggedInUser.getUsername(), e);
            System.out.println("Unable to retrieve your posts.");
            return;
        }

        if (posts == null || posts.isEmpty()) {
            System.out.println("You have no posts to delete.");
            return;
        }

        System.out.println("========== YOUR POSTS ==========");
        displayPosts(posts);

        System.out.print("Enter the Post ID to delete: ");
        int postId = getMenuChoice(scanner);

        boolean postBelongsToUser = false;
        for (Post post : posts) {
            if (post.getPost_id() == postId) {
                postBelongsToUser = true;
                break;
            }
        }

        if (!postBelongsToUser) {
            System.out.println("Invalid Post ID. Please select one of your posts.");
            return;
        }

        try {
            boolean result = postController.deletePost(
                    postId,
                    loggedInUser.getUsername()
            );

            if (result) {
                System.out.println("Post deleted successfully!");
            } else {
                System.out.println("Post deletion failed. Please try again.");
            }
        } catch (Exception e) {
            logger.error("Error deleting post: post_id={}, username={}", postId, loggedInUser.getUsername(), e);
            System.out.println("Post deletion failed due to an unexpected error.");
        }
    }


    private static void displayPosts(List<Post> posts) {

        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts available.");
            return;
        }

        for (Post post : posts) {
            System.out.println("Post ID        : " + post.getPost_id());
            System.out.println("Caption        : " + post.getCaption());
            System.out.println("Image          : " + post.getImage_url());
            System.out.println("----------------------------");
        }
    }


// =====================================================
// 3. SEARCH PROFILE
// =====================================================

    private static void searchProfile(
            Scanner scanner,
            UserController userController,
            ProfileController profileController,
            PostController postController) {


        System.out.println();
        System.out.println("========== SEARCH PROFILE ==========");

        System.out.print("Enter username to search: ");

        String username =
                scanner.nextLine();

        // ==========================================
        // FIND USER
        // ==========================================

        User user;

        try {
            user = userController.searchUserByUsername(username);
        } catch (UserNotFoundException e) {

            System.out.println(e.getMessage());

            return;
        }


        if (user == null) {

            System.out.println("Profile not found.");

            return;
        }


        // ==========================================
        // FIND PROFILE
        // ==========================================

        Profile profile;

        try {

            profile =
                    profileController.findProfileByUserId(
                            user.getUser_id()
                    );

        } catch (Exception e) {

            logger.error("Error retrieving profile: user_id={}", user.getUser_id(), e);

            System.out.println("Unable to retrieve profile.");

            return;
        }


        if (profile == null) {

            System.out.println("Profile not found.");

            return;
        }


        // ==========================================
        // DISPLAY PROFILE
        // ==========================================

        System.out.println();
        System.out.println("========== PROFILE FOUND ==========");

        System.out.println("Username       : " + user.getUsername());

        System.out.println("Bio            : " + profile.getBio());

        System.out.println("Profile Image  : " + profile.getProfile_image_url());


        // ==========================================
        // FIND POSTS
        // ==========================================

        List<Post> posts;

        try {

            posts =
                    postController.findPostsByUsername(
                            user.getUsername()
                    );

        } catch (Exception e) {

            logger.error("Error retrieving posts: username={}", user.getUsername(), e);

            System.out.println("Unable to retrieve posts.");

            return;
        }


        // ==========================================
        // DISPLAY POSTS
        // ==========================================

        System.out.println();
        System.out.println("========== POSTS ==========");

        if (posts == null || posts.isEmpty()) {

            System.out.println("No posts available.");

        } else {

            for (Post post : posts) {

                System.out.println("Post ID        : " + post.getPost_id());

                System.out.println("Caption        : " + post.getCaption());

                System.out.println("Image          : " + post.getImage_url());

                System.out.println("----------------------------");
            }
        }


        System.out.println("===================================");
    }

    // =====================================================
    // LIKE POST
    // =====================================================

    private static void likePost(
            Scanner scanner,
            User loggedInUser,
            PostController postController,
            LikeController likeController) {

        System.out.println();
        System.out.println("========== FEED ==========");

        List<Post> posts;

        try {
            posts = postController.findAllPosts();
        } catch (Exception e) {
            logger.error("Error retrieving feed.", e);
            System.out.println("Unable to retrieve feed.");
            return;
        }

        List<Post> otherUserPosts = new java.util.ArrayList<>();

        if (posts != null) {
            for (Post post : posts) {
                if (post != null
                        && post.getUser() != null
                        && post.getUser().getUser_id() != loggedInUser.getUser_id()) {
                    otherUserPosts.add(post);
                }
            }
        }

        if (otherUserPosts.isEmpty()) {
            System.out.println("No posts from other users are available.");
            return;
        }

        displayFeedPosts(otherUserPosts, likeController, loggedInUser);

        System.out.print("Enter the Post ID to like: ");
        int postId = getMenuChoice(scanner);

        Post selectedPost = findPostById(otherUserPosts, postId);

        if (selectedPost == null) {
            System.out.println("Invalid post ID. Please select a post from the feed.");
            return;
        }

        if (hasUserLikedPost(selectedPost.getPost_id(), loggedInUser.getUser_id(), likeController)) {
            System.out.println("You have already liked this post.");
            return;
        }

        Like like = new Like();
        like.setUser(loggedInUser);
        like.setPost(selectedPost);

        try {
            boolean result = likeController.createLike(like);

            if (result) {
                System.out.println("Post liked successfully!");
            } else {
                System.out.println("Unable to like the post.");
            }

        } catch (Exception e) {
            logger.error("Error liking post: post_id={}, user_id={}", postId, loggedInUser.getUser_id(), e);
            System.out.println("Unable to like the post.");
        }
    }


    // =====================================================
    // UNLIKE POST
    // =====================================================

    private static void unlikePost(
            Scanner scanner,
            User loggedInUser,
            PostController postController,
            LikeController likeController) {

        System.out.println();
        System.out.println("========== FEED ==========");

        List<Post> posts;

        try {
            posts = postController.findAllPosts();
        } catch (Exception e) {
            logger.error("Error retrieving feed.", e);
            System.out.println("Unable to retrieve feed.");
            return;
        }

        List<Post> otherUserPosts = new java.util.ArrayList<>();

        if (posts != null) {
            for (Post post : posts) {
                if (post != null
                        && post.getUser() != null
                        && post.getUser().getUser_id() != loggedInUser.getUser_id()) {
                    otherUserPosts.add(post);
                }
            }
        }

        if (otherUserPosts.isEmpty()) {
            System.out.println("No posts from other users are available.");
            return;
        }

        displayFeedPosts(otherUserPosts, likeController, loggedInUser);

        System.out.print("Enter the Post ID to unlike: ");
        int postId = getMenuChoice(scanner);

        Post selectedPost = findPostById(otherUserPosts, postId);

        if (selectedPost == null) {
            System.out.println("Invalid post ID. Please select a post from the feed.");
            return;
        }

        if (!hasUserLikedPost(selectedPost.getPost_id(), loggedInUser.getUser_id(), likeController)) {
            System.out.println("You have not liked this post.");
            return;
        }

        try {
            boolean result =
                    likeController.deleteLike(
                            loggedInUser.getUser_id(),
                            selectedPost.getPost_id()
                    );

            if (result) {
                System.out.println("Post unliked successfully!");
            } else {
                System.out.println("Unable to unlike the post.");
            }

        } catch (Exception e) {
            logger.error("Error unliking post: post_id={}, user_id={}", postId, loggedInUser.getUser_id(), e);
            System.out.println("Unable to unlike the post.");
        }
    }


    // =====================================================
    // DISPLAY FEED POSTS
    // =====================================================

    private static void displayFeedPosts(
            List<Post> posts,
            LikeController likeController,
            User loggedInUser) {

        for (Post post : posts) {

            int likeCount;
            boolean likedByCurrentUser;

            try {
                likeCount = likeController.countLikesByPostId(post.getPost_id());
                likedByCurrentUser = hasUserLikedPost(
                        post.getPost_id(),
                        loggedInUser.getUser_id(),
                        likeController
                );
            } catch (Exception e) {
                logger.error("Error retrieving likes: post_id={}", post.getPost_id(), e);
                likeCount = 0;
                likedByCurrentUser = false;
            }

            System.out.println();
            System.out.println("Post ID        : " + post.getPost_id());
            System.out.println("Posted By      : " + post.getUser().getUsername());
            System.out.println("Caption        : " + post.getCaption());
            System.out.println("Image          : " + post.getImage_url());
            System.out.println("Likes          : " + likeCount);
            System.out.println("You            : " + (likedByCurrentUser ?"Liked" :"Not Liked"));
            System.out.println("----------------------------");
        }
    }


    // =====================================================
    // FIND POST IN FEED
    // =====================================================

    private static Post findPostById(
            List<Post> posts,
            int postId) {

        for (Post post : posts) {
            if (post.getPost_id() == postId) {
                return post;
            }
        }

        return null;
    }


    // =====================================================
    // CHECK WHETHER USER LIKED POST
    // =====================================================

    private static boolean hasUserLikedPost(
            int postId,
            int userId,
            LikeController likeController) {

        List<Like> likes = likeController.findLikesByPostId(postId);

        if (likes == null) {
            return false;
        }

        for (Like like : likes) {
            if (like != null
                    && like.getUser() != null
                    && like.getUser().getUser_id() == userId) {
                return true;
            }
        }

        return false;
    }



    // =====================================================
    // 9. COMMENT ON POST
    // =====================================================

    private static void commentOnPost(
            Scanner scanner,
            User loggedInUser,
            PostController postController,
            CommentController commentController) {

        System.out.println();
        System.out.println("========== COMMENT ON POST ==========");

        List<Post> posts;

        try {
            posts = postController.findAllPosts();
        } catch (Exception e) {
            logger.error("Error retrieving posts for comments.", e);
            System.out.println("Unable to retrieve posts.");
            return;
        }

        // Only display posts created by other users
        List<Post> otherUserPosts = new java.util.ArrayList<>();

        if (posts != null) {
            for (Post post : posts) {
                if (post != null
                        && post.getUser() != null
                        && post.getUser().getUser_id() != loggedInUser.getUser_id()) {
                    otherUserPosts.add(post);
                }
            }
        }

        if (otherUserPosts.isEmpty()) {
            System.out.println("No posts from other users are available for commenting.");
            return;
        }

        displayPosts(otherUserPosts);

        System.out.print("Enter the Post ID to comment on: ");
        int postId = getMenuChoice(scanner);

        Post selectedPost = findPostById(otherUserPosts, postId);

        if (selectedPost == null) {
            System.out.println("Invalid Post ID. Please select an available post.");
            return;
        }

        System.out.print("Enter your comment: ");
        String commentText = scanner.nextLine();

        if (commentText == null || commentText.trim().isEmpty()) {
            System.out.println("Comment cannot be empty.");
            return;
        }

        Comment comment = new Comment();
        comment.setUser(loggedInUser);
        comment.setPost(selectedPost);
        comment.setComment_text(commentText);

        try {
            boolean result = commentController.createComment(comment);

            if (result) {
                System.out.println("Comment added successfully!");
                displayComments(selectedPost.getPost_id(), commentController);
            } else {
                System.out.println("Comment creation failed. Please try again.");
            }

        } catch (Exception e) {
            logger.error("Error creating comment: post_id={}, user_id={}", selectedPost.getPost_id(), loggedInUser.getUser_id(), e);
            System.out.println("Comment creation failed due to an unexpected error.");
        }
    }


    // =====================================================
    // 10. DELETE COMMENT
    // =====================================================

    private static void deleteComment(
            Scanner scanner,
            User loggedInUser,
            PostController postController,
            CommentController commentController) {

        System.out.println();
        System.out.println("========== DELETE COMMENT ==========");

        List<Post> posts;

        try {
            posts = postController.findAllPosts();
        } catch (Exception e) {
            logger.error("Error retrieving posts for comment deletion.", e);
            System.out.println("Unable to retrieve posts.");
            return;
        }

        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts available.");
            return;
        }

        displayPosts(posts);

        System.out.print("Enter the Post ID containing your comment: ");
        int postId = getMenuChoice(scanner);

        Post selectedPost = findPostById(posts, postId);

        if (selectedPost == null) {
            System.out.println("Invalid Post ID. Please select an available post.");
            return;
        }

        List<Comment> comments;

        try {
            comments = commentController.findCommentsByPostId(postId);
        } catch (Exception e) {
            logger.error("Error retrieving comments: post_id={}", postId, e);
            System.out.println("Unable to retrieve comments.");
            return;
        }

        if (comments == null || comments.isEmpty()) {
            System.out.println("No comments available for this post.");
            return;
        }

        displayComments(comments);

        System.out.print("Enter the Comment ID to delete: ");
        int commentId = getMenuChoice(scanner);

        Comment selectedComment = null;

        for (Comment comment : comments) {
            if (comment != null && comment.getComment_id() == commentId) {
                selectedComment = comment;
                break;
            }
        }
        if (selectedComment == null) {
            System.out.println("Invalid Comment ID.");
            return;
        }

        if (selectedComment.getUser() == null
                || selectedComment.getUser().getUser_id() != loggedInUser.getUser_id()) {
            System.out.println("You can delete only your own comments.");
            return;
        }

        Comment commentToDelete = new Comment();
        commentToDelete.setComment_id(commentId);
        commentToDelete.setUser(loggedInUser);
        commentToDelete.setPost(selectedPost);

        try {
            boolean result = commentController.deleteComment(commentToDelete);

            if (result) {
                System.out.println("Comment deleted successfully!");
            } else {
                System.out.println("Comment deletion failed. Please try again.");
            }

        } catch (Exception e) {
            logger.error("Error deleting comment: comment_id={}, user_id={}", commentId, loggedInUser.getUser_id(), e);
            System.out.println("Comment deletion failed due to an unexpected error.");
        }
    }
    // =====================================================
    // DISPLAY COMMENTS

    private static void displayComments(
            int postId,
            CommentController commentController) {

        try {
            List<Comment> comments = commentController.findCommentsByPostId(postId);
            displayComments(comments);
        } catch (Exception e) {
            logger.error("Error retrieving comments: post_id={}", postId, e);
            System.out.println("Unable to retrieve comments.");
        }
    }
    private static void displayComments(List<Comment> comments) {

        System.out.println();
        System.out.println("========== COMMENTS ==========");

        if (comments == null || comments.isEmpty()) {
            System.out.println("No comments available.");
            return;
        }

        for (Comment comment : comments) {
            if (comment == null) {
                continue;
            }

            String username =
                    comment.getUser() != null
                            ? comment.getUser().getUsername()
                            : "Unknown User";

            System.out.println("Comment ID : " + comment.getComment_id());
            System.out.println("Commented By: " + username);
            System.out.println("Comment     : " + comment.getComment_text());
            System.out.println("----------------------------");
        }
    }
    // 11. FOLLOW USER
    private static void followUser(
            Scanner scanner,
            User loggedInUser,
            UserController userController,
            FollowController followController) {

        System.out.println();
        System.out.println("========== FOLLOW USER ==========");

        System.out.print("Enter username to follow: ");

        String username = scanner.nextLine();

        User targetUser;

        try {

            targetUser =
                    userController.searchUserByUsername(username);

        } catch (UserNotFoundException e) {

            System.out.println(e.getMessage());

            return;

        } catch (IllegalArgumentException e) {

            System.out.println("Username cannot be empty.");

            return;

        } catch (Exception e) {

            logger.error("Error while searching user to follow: username={}", username, e);

            System.out.println("Unable to find the user.");

            return;
        }

        if (targetUser == null) {

            System.out.println("User not found.");

            return;
        }

        Follow follow = new Follow();

        follow.setFollower(loggedInUser);
        follow.setFollowing(targetUser);

        boolean result;

        try {

            result =
                    followController.createFollow(follow);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {

            logger.error("Error while following user: follower_id={}, following_id={}", loggedInUser.getUser_id(), targetUser.getUser_id(), e);

            System.out.println("Unable to follow the user.");

            return;
        }

        if (result) {

            System.out.println("You are now following " + targetUser.getUsername() +"!");

        } else {

            System.out.println("You are already following " + targetUser.getUsername() +".");
        }
    }


    // =====================================================
    // 10. UNFOLLOW USER
    // =====================================================

    private static void unfollowUser(
            Scanner scanner,
            User loggedInUser,
            UserController userController,
            FollowController followController) {

        System.out.println();
        System.out.println("========== UNFOLLOW USER ==========");

        System.out.print("Enter username to unfollow: ");

        String username = scanner.nextLine();

        User targetUser;

        try {

            targetUser =
                    userController.searchUserByUsername(username);

        } catch (UserNotFoundException e) {

            System.out.println(e.getMessage());

            return;

        } catch (IllegalArgumentException e) {

            System.out.println("Username cannot be empty.");

            return;

        } catch (Exception e) {

            logger.error("Error while searching user to unfollow: username={}", username, e);

            System.out.println("Unable to find the user.");

            return;
        }

        if (targetUser == null) {

            System.out.println("User not found.");

            return;
        }


        boolean result;

        try {

            result =
                    followController.deleteFollow(
                            loggedInUser.getUser_id(),
                            targetUser.getUser_id()
                    );

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());

            return;

        } catch (Exception e) {

            logger.error("Error while unfollowing user: follower_id={}, following_id={}", loggedInUser.getUser_id(), targetUser.getUser_id(), e);

            System.out.println("Unable to unfollow the user.");

            return;
        }
        if (result) {

            System.out.println("You have unfollowed " + targetUser.getUsername() +".");

        } else {

            System.out.println("You are not following " + targetUser.getUsername() +".");
        }
    }
    // =====================================================
    // 11. VIEW FOLLOWERS
    // =====================================================
    private static void viewFollowers(
            User loggedInUser,
            FollowController followController) {
        System.out.println();
        System.out.println("========== YOUR FOLLOWERS ==========");
        List<Follow> followers;
        try {
            followers = followController.findFollowersByUsername(loggedInUser.getUsername());
        } catch (Exception e) {
            logger.error("Error while retrieving followers: username={}", loggedInUser.getUsername(), e);
            System.out.println("Unable to retrieve followers.");
            return;
        }
        if (followers == null ||
                followers.isEmpty()) {

            System.out.println("You have no followers.");

            return;
        }

        int count = 1;

        for (Follow follow : followers) {

            if (follow != null &&
                    follow.getFollower() != null) {
                System.out.println(count +". " + follow.getFollower().getUsername());
                count++;
            }
        }
        System.out.println("Total Followers : " + followers.size());
    }
    // 12. VIEW FOLLOWING
    // =====================================================
    private static void viewFollowing(
            User loggedInUser,
            FollowController followController) {

        System.out.println();
        System.out.println("========== YOU ARE FOLLOWING ==========");

        List<Follow> following;

        try {

            following =
                    followController.findFollowingByUsername(
                            loggedInUser.getUsername()
                    );

        } catch (Exception e) {

            logger.error("Error while retrieving following users: username={}", loggedInUser.getUsername(), e);

            System.out.println("Unable to retrieve following users.");

            return;
        }

        if (following == null ||
                following.isEmpty()) {

            System.out.println("You are not following anyone.");

            return;
        }

        int count = 1;

        for (Follow follow : following) {

            if (follow != null &&
                    follow.getFollowing() != null) {

                System.out.println(count +". " + follow.getFollowing().getUsername());

                count++;
            }
        }

        System.out.println("Total Following : " + following.size());
    }


    // =====================================================
    // ADMIN LOGIN
    // =====================================================

    private static void adminLogin(
            Scanner scanner,
            UserController userController) {

        System.out.println();
        System.out.println("========== ADMIN LOGIN ==========");

        String username = readRequiredInput(
                scanner,
                "Enter username: ",
                "Username cannot be empty."
        );

        String password = readRequiredInput(
                scanner,
                "Enter password: ",
                "Password cannot be empty."
        );

        User admin;
        try {
            admin = userController.searchUserByUsername(username);
        } catch (UserNotFoundException e) {
            System.out.println("No such user exists.");
            return;
        } catch (Exception e) {

            logger.error("Error while admin login: username={}", username, e);
            System.out.println("Admin login failed due to an unexpected error.");
            return;
        }

        if (admin == null) {
            System.out.println("Invalid admin username or password.");
            return;
        }
        if (!admin.getPassword_hash().equals(password)) {
            System.out.println("Invalid admin username or password.");
            return;
        }
        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            System.out.println(
                    "Access denied. Admin privileges required."
            );

            return;
        }
        System.out.println("Admin login successful!");

        adminOperationsMenu(
                scanner,
                userController
        );
    }
    // ADMIN OPERATIONS
    private static void adminOperationsMenu(
            Scanner scanner,
            UserController userController) {

        boolean adminLoggedIn = true;

        while (adminLoggedIn) {

            System.out.println();
            System.out.println("========== ADMIN OPERATIONS ==========");

            System.out.println("1. Count Users");
            System.out.println("2. Users by Status");
            System.out.println("3. Logout");

            System.out.print("Enter your choice: ");

            int choice = getMenuChoice(scanner);

            switch (choice) {
                case 1:
                    try {
                        int totalUsers = userController.countTotalUsers();
                        System.out.println();
                        System.out.println("Total Users: " + totalUsers);
                    } catch (Exception e) {
                        logger.error("Error counting users.", e);
                        System.out.println("Unable to count users.");
                    }
                    break;
                case 2:
                    try {
                        Map<String, Integer> statusCounts = userController.countUsersByStatus();
                        if (statusCounts == null ||
                                statusCounts.isEmpty()) {
                            System.out.println("No user status details found.");
                        } else {
                            System.out.println();
                            System.out.println("========== USERS BY STATUS ==========");
                            int activeUsers = statusCounts.getOrDefault("ACTIVE", 0);
                            int inactiveUsers = statusCounts.getOrDefault("INACTIVE", 0);
                            System.out.println("Active Users : " + activeUsers);
                            if (inactiveUsers > 0) {
                                System.out.println("Inactive Users : " + inactiveUsers);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error retrieving users by status.", e);
                        System.out.println("Unable to retrieve users by status.");
                    }
                    break;
                case 3:
                    System.out.println("Admin logout successful!");
                    adminLoggedIn = false;
                    break;
                default:
                    logger.warn("Invalid admin operation choice: {}", choice);
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    // CHECK WHETHER PHONE NUMBER ALREADY EXISTS
    private static boolean isPhoneAlreadyUsed(
            String phone,
            ProfileController profileController) {

        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        try {
            List<Profile> profiles =
                    profileController.findAllProfiles();

            if (profiles == null || profiles.isEmpty()) {
                return false;
            }
            for (Profile existingProfile : profiles) {
                if (existingProfile == null
                        || existingProfile.getPhone() == null) {
                    continue;
                }
                if (existingProfile.getPhone().trim().equals(phone.trim())) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("Error while checking existing phone number", e);
        }
        return false;
    }
}
