package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.UserDto;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.request.UserLoginRequest;
import com.linielt.realworldapispringboot.request.UserRegistrationRequest;
import com.linielt.realworldapispringboot.request.UserUpdateRequest;
import com.linielt.realworldapispringboot.service.JwtTokenProviderService;
import com.linielt.realworldapispringboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {

    private final UserService userService;
    private final JwtTokenProviderService tokenProviderService;

    public UserRestController(UserService userService, JwtTokenProviderService tokenProviderService) {
        this.userService = userService;
        this.tokenProviderService = tokenProviderService;
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        var user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new ResponseEntity<>(UserDto.fromUserAndTokenValueToDto(user, tokenProviderService.generateToken(user)),
                HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> register(@RequestBody UserRegistrationRequest registrationRequest) {
        var registeredUser = userService.register(registrationRequest);
        var userDto = UserDto.fromUserAndTokenValueToDto(registeredUser, tokenProviderService.generateToken(registeredUser));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getCurrentUser(JwtAuthenticationToken jwtToken) {
        var currentUser = userService.getUserByToken(jwtToken);
        return new ResponseEntity<>(UserDto.fromUserAndTokenValueToDto(currentUser, jwtToken.getToken().getTokenValue()), HttpStatus.OK);
    }

    @PatchMapping("/user")
    public ResponseEntity<UserDto> updateCurrentUser(JwtAuthenticationToken jwtToken, @Valid @RequestBody UserUpdateRequest updateRequest) {
        User currentUser = userService.getUserByToken(jwtToken);

        var updatedUser = userService.updateCurrentUser(currentUser, updateRequest);
        return new ResponseEntity<>(UserDto.fromUserAndTokenValueToDto(updatedUser, jwtToken.getToken().getTokenValue()), HttpStatus.OK);
    }
}
