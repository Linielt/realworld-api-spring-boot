package com.linielt.realworldapispringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linielt.realworldapispringboot.dtos.ProfileDto;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.rest.controller.ProfileRestController;
import com.linielt.realworldapispringboot.security.SecurityConfig;
import com.linielt.realworldapispringboot.service.JwtTokenProviderService;
import com.linielt.realworldapispringboot.service.ProfileService;
import com.linielt.realworldapispringboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileRestController.class)
@Import(SecurityConfig.class)
public class ProfileRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ProfileService profileService;
    @MockBean
    UserService userService;
    @MockBean
    JwtTokenProviderService jwtTokenProviderService;

    private User testUser;
    private User testUserForFollows;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        testUser = User.createUser("Jacob", "jake@jake.jake", passwordEncoder.encode("jakejake"));
        testUserForFollows = User.createUser("updated", "updated@update.update", passwordEncoder.encode("updated"));
    }

    @Test
    @DisplayName("When invoked using valid username whilst unauthenticated, should return profile of user with following as false")
    void getProfileFromUsernameUnauthenticatedTest() throws Exception {
        when(userService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        when(profileService.getProfile(testUser)).thenReturn(ProfileDto.fromUser(testUser));

        mockMvc.perform(get("/profiles/" + testUser.getUsername())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.profile.bio", is(testUser.getBio())))
                .andExpect(jsonPath("$.profile.image", is(testUser.getImage())))
                .andExpect(jsonPath("$.profile.following", is(false)));
    }

    @Test
    @DisplayName("When invoked using invalid username, return 404 response code")
    void getProfileFromInvalidUsernameTest() throws Exception {
        when(userService.getUserByUsername(testUser.getUsername())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/profiles/" + testUser.getUsername())
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When invoked with valid username and whilst authenticated," +
            " return profile DTO with correct follow status and 200 response code")
    void getProfileFromUsernameAuthenticatedTest() throws Exception {
        when(userService.getUserByUsername(testUserForFollows.getUsername())).thenReturn(testUserForFollows);
        when(profileService.getProfile(testUserForFollows)).thenReturn(ProfileDto.fromUser(testUserForFollows));
        when(userService.getUserByToken(any(JwtAuthenticationToken.class))).thenReturn(testUser);

        testUser.followUser(testUserForFollows);

        mockMvc.perform(get("/profiles/" + testUserForFollows.getUsername())
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username", is(testUserForFollows.getUsername())))
                .andExpect(jsonPath("$.profile.bio", is(testUserForFollows.getBio())))
                .andExpect(jsonPath("$.profile.image", is(testUserForFollows.getImage())))
                .andExpect(jsonPath("$.profile.following", is(testUser.isFollowing(testUserForFollows))));
    }

    @Test
    @DisplayName("When invoked with invalid username, return 404 response code")
    void followInvalidUserTest() throws Exception {
        when(userService.getUserByUsername(testUserForFollows.getUsername())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/profiles/" + testUserForFollows.getUsername() + "/follow")
                .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When invoked with invalid username, return 404 response code")
    void unfollowInvalidUserTest() throws Exception {
        when(userService.getUserByUsername(testUserForFollows.getUsername())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(delete("/profiles/" + testUserForFollows.getUsername() + "/follow")
                .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When invoked whilst unauthorized, return 401 response code")
    void followUserUnauthorizedTest() throws Exception {
        when(userService.getUserByUsername(testUserForFollows.getUsername())).thenReturn(testUserForFollows);

        mockMvc.perform(post("/profile/" + testUserForFollows.getUsername() + "/follow")
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("When invoked whilst unauthorized, return 401 response code")
    void unfollowUserUnauthorizedTest() throws Exception {
        when(userService.getUserByUsername(testUserForFollows.getUsername())).thenReturn(testUserForFollows);

        mockMvc.perform(delete("/profile/" + testUserForFollows.getUsername() + "/follow")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}
