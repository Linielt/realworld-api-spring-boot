package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.dtos.ProfileDto;
import com.linielt.realworldapispringboot.model.User;
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

    public ProfileDto getProfile(User targetUser) {
        return ProfileDto.fromUser(targetUser);
    }

    @Transactional
    public ProfileDto followProfile(JwtAuthenticationToken jwtToken, User targetUser) {
        var currentUser = userService.getUserByToken(jwtToken);

        userRepository.save(currentUser.followUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }

    @Transactional
    public ProfileDto unfollowProfile(JwtAuthenticationToken jwtToken, User targetUser) {
        var currentUser = userService.getUserByToken(jwtToken);

        userRepository.save(currentUser.unfollowUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }
}
