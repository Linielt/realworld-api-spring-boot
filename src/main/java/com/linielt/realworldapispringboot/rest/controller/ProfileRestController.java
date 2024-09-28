package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.ProfileDto;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.service.ProfileService;
import com.linielt.realworldapispringboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class ProfileRestController {
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileRestController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/profiles/{username}")
    public ResponseEntity<ProfileDto> getProfile(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        User targetUser = null;
        try {
            targetUser = userService.getUserByUsername(username);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ProfileDto dto = profileService.getProfile(targetUser);

        if (jwtToken == null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        User currentUser = userService.getUserByToken(jwtToken);
        dto.setFollowing(currentUser.isFollowing(targetUser));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileDto> followProfile(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        try {
            User currentUser = userService.getUserByToken(jwtToken);
            User targetUser = userService.getUserByUsername(username);

            return new ResponseEntity<>(profileService.followProfile(currentUser, targetUser), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileDto> unfollowUser(JwtAuthenticationToken jwtToken, @PathVariable String username) {
        try {
            User currentUser = userService.getUserByToken(jwtToken);
            User targetUser = userService.getUserByUsername(username);

            return new ResponseEntity<>(profileService.unfollowProfile(currentUser, targetUser), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
