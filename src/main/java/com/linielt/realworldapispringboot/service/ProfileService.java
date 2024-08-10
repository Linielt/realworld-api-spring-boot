package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.dtos.ProfileDto;
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

    public ProfileDto getProfileFromUsername(JwtAuthenticationToken jwtToken, String username) {
        var targetUser = userRepository.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);
        // TODO - Use ResponseEntity in controller to give better error message and status code rather than 500.
        if (jwtToken == null) {
            return ProfileDto.fromUser(targetUser, false);
        }

        var currentUser = userRepository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(NoSuchElementException::new);

        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }

    @Transactional
    public ProfileDto followProfile(JwtAuthenticationToken jwtToken, String username) {
        var currentUser = userRepository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(NoSuchElementException::new);
        var targetUser = userRepository.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);

        userRepository.save(currentUser.followUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }

    @Transactional
    public ProfileDto unfollowProfile(JwtAuthenticationToken jwtToken, String username) {
        var currentUser = userRepository.findUserById(Integer.parseInt(jwtToken.getName()))
                .orElseThrow(NoSuchElementException::new);
        var targetUser = userRepository.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);

        userRepository.save(currentUser.unfollowUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }
}
