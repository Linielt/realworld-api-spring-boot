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
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User login(UserLoginRequest loginRequest) {
        return repository.findUserByEmail(loginRequest.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password."));
    }

    @Transactional
    public User register(UserRegistrationRequest registrationRequest) {
        var user = User.fromRegistrationRequest(registrationRequest);
        user.encryptAndSetPassword(registrationRequest.getPassword(), passwordEncoder);
        return repository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserByToken(JwtAuthenticationToken jwtToken) {
        return repository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(() -> new NoSuchElementException("User could not be found."));
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User could not be found."));
    }

    // TODO - Allow for user to set fields to null
    @Transactional
    public User updateCurrentUser(User currentUser, UserUpdateRequest updateRequest) {
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
