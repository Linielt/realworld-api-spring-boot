package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.dtos.ProfileDto;
import com.linielt.realworldapispringboot.repository.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final UserService userService;

    public ProfileService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public ProfileDto getProfileFromUsername(JwtAuthenticationToken jwtToken, String targetUsername) {
        var targetUser = userService.getUserByUsername(targetUsername);
        // TODO - Use ResponseEntity in controller to give better error message and status code rather than 500.
        if (jwtToken == null) {
            return ProfileDto.fromUser(targetUser, false);
        }

        var currentUser = userService.getUserByToken(jwtToken);

        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }

    @Transactional
    public ProfileDto followProfile(JwtAuthenticationToken jwtToken, String targetUsername) {
        var currentUser = userService.getUserByToken(jwtToken);
        var targetUser = userService.getUserByUsername(targetUsername);

        userRepository.save(currentUser.followUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }

    @Transactional
    public ProfileDto unfollowProfile(JwtAuthenticationToken jwtToken, String targetUsername) {
        var currentUser = userService.getUserByToken(jwtToken);
        var targetUser = userService.getUserByUsername(targetUsername);

        userRepository.save(currentUser.unfollowUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }
}
