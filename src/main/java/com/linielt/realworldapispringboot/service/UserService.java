package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.repository.UserRepository;
import com.linielt.realworldapispringboot.request.UserLoginRequest;
import com.linielt.realworldapispringboot.request.UserRegistrationRequest;
import com.linielt.realworldapispringboot.request.UserUpdateRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(UserLoginRequest loginRequest) {
        return repository.findUserByEmail(loginRequest.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password."));
    }

    public User register(UserRegistrationRequest registrationRequest) {
        var user = User.fromRegistrationRequest(registrationRequest);
        user.encryptAndSetPassword(registrationRequest.getPassword(), passwordEncoder);
        return repository.save(user);
    }

    public User getCurrentUser(JwtAuthenticationToken jwtToken) {
        return repository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(NoSuchElementException::new);
    }

    // TODO - Allow for user to set fields to null
    public User updateCurrentUser(JwtAuthenticationToken jwtToken, UserUpdateRequest updateRequest) {
        var currentUser = repository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(NoSuchElementException::new); // TODO - Check if I need to throw an exception here or not.

        if (updateRequest.email() != null && !updateRequest.email().isEmpty()) {
            currentUser.setEmail(updateRequest.email());
        }
        if (updateRequest.username() != null && !updateRequest.username().isEmpty()) {
            currentUser.setUsername(updateRequest.username());
        }
        if (updateRequest.bio() != null) {
            currentUser.setBio(updateRequest.bio());
        }
        if (updateRequest.image() != null) {
            currentUser.setImage(updateRequest.image());
        }
        if (updateRequest.rawPassword() != null && !updateRequest.rawPassword().isEmpty()) {
            currentUser.encryptAndSetPassword(updateRequest.rawPassword(), passwordEncoder);
        }


        return repository.save(currentUser);
    }
}