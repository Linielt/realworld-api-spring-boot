package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.repository.UserRepository;
import com.linielt.realworldapispringboot.request.UserLoginRequest;
import com.linielt.realworldapispringboot.request.UserRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    private UserService userService;
    private User testUser;
    private User testUpdatedUser;
    private UserLoginRequest testLoginRequest;
    private UserRegistrationRequest testRegistrationRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.userService = new UserService(userRepository, passwordEncoder);

        testUser = User.createUser("Jacob", "jake@jake.jake", passwordEncoder.encode("jakejake"));
        testUpdatedUser = User.createUser("updated", "update@update.update", passwordEncoder.encode("updated"));

        testRegistrationRequest = new UserRegistrationRequest("Jacob", "jake@jake.jake", "jakejake");
        testLoginRequest = new UserLoginRequest("jake@jake.jake", "jakejake");
    }

    @Test
    @DisplayName("When invoked with correct user details, returned User is not null and has the same email")
    void loginWithCorrectCredentialsTest() {
        when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        var userToLogin = userService.login(testLoginRequest.getEmail(), testLoginRequest.getPassword());

        assertThat(userToLogin).isNotNull();
        assertThat(userToLogin.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void loginWithIncorrectCredentialsTest() {
        UserLoginRequest loginRequestWithWrongPassword = new UserLoginRequest("jake@jake.jake", "wrong");
        when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> userService.login
                        (loginRequestWithWrongPassword.getEmail(), loginRequestWithWrongPassword.getPassword()));
    }

    @Test
    void registerUserTest() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        var userToRegister = userService.register(testRegistrationRequest);

        assertThat(userToRegister.getId()).isNotNull();
        assertThat(userToRegister.getPassword()).isNotBlank();
        assertThat(userToRegister.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(userToRegister.getBio()).isEqualTo(testUser.getBio());
        assertThat(userToRegister.getImage()).isEqualTo(testUser.getImage());
    }

    @Test
    void getUserByUsernameWithExistingUsernameTest() {
        when(userRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        var userToGet = userService.getUserByUsername("Jacob");

        assertThat(userToGet.getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    void getUserWithNonExistingUsernameTest() {
        when(userRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.getUserByUsername("someNonExistantUser"));
    }
}
