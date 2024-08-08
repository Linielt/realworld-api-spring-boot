package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.Profile;
import com.linielt.realworldapispringboot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Profile getProfileFromUsername(String username) {
        var user = userRepository.findUserByUsername(username)
                .orElseThrow(NoSuchElementException::new);
        return Profile.fromUser(user, false); // TODO - Create following system
    }
}
