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

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ProfileDto getProfile(User targetUser) {
        return ProfileDto.fromUser(targetUser);
    }

    @Transactional
    public ProfileDto followProfile(User currentUser, User targetUser) {
        userRepository.save(currentUser.followUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }

    @Transactional
    public ProfileDto unfollowProfile(User currentUser, User targetUser) {
        userRepository.save(currentUser.unfollowUser(targetUser));
        return ProfileDto.fromUser(targetUser, currentUser.isFollowing(targetUser));
    }
}
