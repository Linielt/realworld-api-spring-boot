package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.UserDto;
import com.linielt.realworldapispringboot.mapper.UserMapper;
import com.linielt.realworldapispringboot.request.UserLoginRequest;
import com.linielt.realworldapispringboot.request.UserRegistrationRequest;
import com.linielt.realworldapispringboot.request.UserUpdateRequest;
import com.linielt.realworldapispringboot.service.JwtTokenProviderService;
import com.linielt.realworldapispringboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {

    private final UserService userService;
    private final JwtTokenProviderService tokenProviderService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, JwtTokenProviderService tokenProviderService, UserMapper userMapper) {
        this.userService = userService;
        this.tokenProviderService = tokenProviderService;
        this.userMapper = userMapper;
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserDto> login(@RequestBody UserLoginRequest loginRequest) {
        var user = userService.login(loginRequest);
        UserDto userDto = userMapper.toUserDto(userService.login(loginRequest));
        userDto.setToken(tokenProviderService.generateToken(user));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> register(@RequestBody UserRegistrationRequest registrationRequest) {
        var registeredUser = userService.register(registrationRequest);
        var userDto = userMapper.toUserDto(registeredUser);
        userDto.setToken(tokenProviderService.generateToken(registeredUser));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/user")
    public UserDto getCurrentUser(JwtAuthenticationToken jwtToken) {
        var currentUser = userService.getCurrentUser(jwtToken);
        var userDto = userMapper.toUserDto(currentUser);
        userDto.setToken(jwtToken.getToken().getTokenValue());
        return userDto;
    }

    @PatchMapping("/user")
    public UserDto updateCurrentUser(JwtAuthenticationToken jwtToken, @RequestBody UserUpdateRequest updateRequest) {
        var updatedUser = userService.updateCurrentUser(jwtToken, updateRequest);
        var userDto = userMapper.toUserDto(updatedUser);
        userDto.setToken(jwtToken.getToken().getTokenValue());
        return userDto;
    }
}
