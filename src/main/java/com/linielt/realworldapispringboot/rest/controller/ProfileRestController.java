package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.ProfileDto;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.service.ProfileService;
import com.linielt.realworldapispringboot.service.UserService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileRestController {
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileRestController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/profiles/{username}")
    public ProfileDto getProfile(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        User targetUser = userService.getUserByUsername(username);
        ProfileDto dto = profileService.getProfile(targetUser);

        if (jwtToken == null) {
            return dto;
        }

        dto.setFollowing(userService.getUserByToken(jwtToken).isFollowing(targetUser));

        return dto;
    }

    @PostMapping("/profiles/{username}/follow")
    public ProfileDto followProfile(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        User targetUser = userService.getUserByUsername(username);

        return profileService.followProfile(jwtToken, targetUser);
    }

    @DeleteMapping("/profiles/{username}/follow")
    public ProfileDto unfollowUser(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        User targetUser = userService.getUserByUsername(username);

        return profileService.unfollowProfile(jwtToken, targetUser);
    }
}
