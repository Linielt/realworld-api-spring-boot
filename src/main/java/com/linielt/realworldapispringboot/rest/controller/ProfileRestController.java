package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.ProfileDto;
import com.linielt.realworldapispringboot.mapper.ProfileMapper;
import com.linielt.realworldapispringboot.service.ProfileService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileRestController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileRestController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @GetMapping("/profiles/{username}")
    public ProfileDto getProfile(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        return profileMapper.toProfileDto(profileService.getProfileFromUsername(jwtToken, username));
    }

    @PostMapping("/profiles/{username}/follow")
    public ProfileDto followProfile(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        return profileMapper.toProfileDto(profileService.followProfile(jwtToken, username));
    }
}
