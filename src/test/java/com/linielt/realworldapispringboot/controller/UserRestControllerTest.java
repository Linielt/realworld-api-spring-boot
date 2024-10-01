package com.linielt.realworldapispringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.request.UserLoginRequest;
import com.linielt.realworldapispringboot.request.UserUpdateRequest;
import com.linielt.realworldapispringboot.rest.controller.UserRestController;
import com.linielt.realworldapispringboot.security.SecurityConfig;
import com.linielt.realworldapispringboot.service.JwtTokenProviderService;
import com.linielt.realworldapispringboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@Import(SecurityConfig.class)
public class UserRestControllerTest {
    // Call the login endpoint before every authenticated endpoint, extract token, use it in request heading
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenProviderService jwtTokenProviderService;

    private User testUser;
    private User testUpdatedUser;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        testUser = User.createUser("Jacob", "jake@jake.jake", passwordEncoder.encode("jakejake"));
        testUpdatedUser = User.createUser("updated", "updated@update.update", passwordEncoder.encode("updated"));
    }

    @Test
    void testLogin() throws Exception {
        when(userService.login(any(UserLoginRequest.class))).thenReturn(testUser);

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTestUserLoginRequest())))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCurrentUser() throws Exception {
        when(userService.getUserByToken(any(JwtAuthenticationToken.class))).thenReturn(testUser);

        mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.user.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.user.bio", is(testUser.getBio())))
                .andExpect(jsonPath("$.user.image", is(testUser.getImage())));
    }

    @Test
    void updateCurrentUserTest() throws Exception {
        when(userService.getUserByToken(any(JwtAuthenticationToken.class))).thenReturn(testUser);
        when(userService.updateCurrentUser(any(User.class), any(UserUpdateRequest.class))).thenReturn(testUpdatedUser);

        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserUpdateRequest()))
                .with(SecurityMockMvcRequestPostProcessors.jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username", is(testUpdatedUser.getUsername())))
                .andExpect(jsonPath("$.user.email", is(testUpdatedUser.getEmail())))
                .andExpect(jsonPath("$.user.bio", is(testUpdatedUser.getBio())))
                .andExpect(jsonPath("$.user.image", is(testUpdatedUser.getImage())));
    }

    private UserLoginRequest createTestUserLoginRequest() {
        return new UserLoginRequest("jake@jake.jake", "jakejake");
    }

    private UserUpdateRequest createUserUpdateRequest() {
        return new UserUpdateRequest
                ("updated@update.update", "updated", "updated", "updated", "updated");
    }
}
