package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfileServiceTest {
    @Mock
    UserRepository userRepository;
    private ProfileService profileService;
    private User followingUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.profileService = new ProfileService(userRepository);

        followingUser = User.createUser("following", "following@following.follow", "following");
        User followedUser = User.createUser("followed", "followed@followed.follow", "followed");
    }

    @Test
    void testGetProfile() {
        User testUser = User.createUser("user", "user@user.user", "password");

        var profileFromTestUser = profileService.getProfile(testUser);

        assertThat(profileFromTestUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(profileFromTestUser.getImage()).isEqualTo(testUser.getImage());
        assertThat(profileFromTestUser.getBio()).isEqualTo(testUser.getBio());
    }


    @Test
    void testFollowUser() {
        when(userRepository.save(followingUser)).thenReturn(followingUser);
        User followingUser = User.createUser("following", "following@following.follow", "following");
        User followedUser = User.createUser("followed", "followed@followed.follow", "followed");

        var followedUserProfile = profileService.followProfile(followingUser, followedUser);

        assertThat(followedUserProfile.isFollowing()).isTrue();
        assertThat(followingUser.getFollowedUsers().contains(followedUser)).isTrue();
    }

    @Test
    void testUnfollowUser() {
        when(userRepository.save(followingUser)).thenReturn(followingUser);
        User followingUser = User.createUser("following", "following@following.follow", "following");
        User followedUser = User.createUser("followed", "followed@followed.follow", "followed");

        profileService.followProfile(followingUser, followedUser);
        var unfollowedUserProfile = profileService.unfollowProfile(followingUser, followedUser);

        assertThat(unfollowedUserProfile.isFollowing()).isFalse();
        assertThat(followingUser.getFollowedUsers().contains(followedUser)).isFalse();
    }
}
