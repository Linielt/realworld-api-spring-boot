package com.linielt.realworldapispringboot.dtos;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linielt.realworldapispringboot.model.User;

@JsonTypeName("profile")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class ProfileDto {

    private String username;
    private String bio;
    private String image;
    private boolean following;

    protected ProfileDto() {}
    private ProfileDto(String username, String bio, String image, boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public static ProfileDto fromUser(User user, boolean following) {
        return new ProfileDto(user.getUsername(), user.getBio(), user.getImage(), following);
    }

    public static ProfileDto fromUser(User user) {
        return new ProfileDto(user.getUsername(), user.getBio(), user.getImage(), false);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public static class NestedProfileDto {
        private String username;
        private String bio;
        private String image;
        private boolean following;

        protected NestedProfileDto() {}

        public static NestedProfileDto fromProfileDTO(ProfileDto profile, boolean isFollowing) {
            return new NestedProfileDto(profile.getUsername(), profile.getBio(), profile.getImage(), isFollowing);
        }

        public static NestedProfileDto fromProfileDTO(ProfileDto profile) {
            return new NestedProfileDto(profile.getUsername(), profile.getBio(), profile.getImage(), false);
        }

        public static NestedProfileDto fromUser(User user) {
            return new NestedProfileDto(user.getUsername(),
                    user.getBio(),
                    user.getImage(),
                    false);
        }

        private NestedProfileDto(String username, String bio, String image, boolean following) {
            this.username = username;
            this.bio = bio;
            this.image = image;
            this.following = following;
        }

        public String getUsername() {
            return username;
        }

        public String getBio() {
            return bio;
        }

        public String getImage() {
            return image;
        }

        public boolean isFollowing() {
            return following;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }
    }
}
