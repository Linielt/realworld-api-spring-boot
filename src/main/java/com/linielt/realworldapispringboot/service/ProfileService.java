package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.Profile;
import com.linielt.realworldapispringboot.repository.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Profile getProfileFromUsername(JwtAuthenticationToken jwtToken, String username) {
        var targetUser = userRepository.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);
        // TODO - Use ResponseEntity in controller to give better error message and status code rather than 500.
        if (jwtToken == null) {
            return Profile.fromUser(targetUser, false);
        } // TODO - Maybe find a better way to express getting a profile as an unauthenticated user?
          // Consider adding two static fromUser methods or a fromUnauthenticatedUser
        var currentUser = userRepository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(NoSuchElementException::new);

        return Profile.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }

    @Transactional
    public Profile followProfile(JwtAuthenticationToken jwtToken, String username) {
        var currentUser = userRepository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(NoSuchElementException::new);
        var targetUser = userRepository.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);

        userRepository.save(currentUser.followUser(targetUser)); // TODO - Rewrite this method so it reads better
        return Profile.fromUser(targetUser, true);
    }
}
