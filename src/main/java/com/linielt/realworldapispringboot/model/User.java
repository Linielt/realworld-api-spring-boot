package com.linielt.realworldapispringboot.model;

import com.linielt.realworldapispringboot.request.UserRegistrationRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String bio;
    private String image;
    private String password;

    protected User() {}

    private User(String username, String email, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
    }

    public static User fromRegistrationRequest(UserRegistrationRequest registrationRequest) {
        return new User(registrationRequest.getUsername(), registrationRequest.getEmail(),
                registrationRequest.getPassword());
    }

    public void encryptPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(rawPassword);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public String getPassword() {
        return password;
    }
}
