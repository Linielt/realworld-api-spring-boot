package com.linielt.realworldapispringboot.model;

import com.linielt.realworldapispringboot.request.UserRegistrationRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 45, nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(length = 1000)
    private String bio;
    @Column(length = 500)
    private String image;
    @Column(length = 100, nullable = false)
    private String password;

    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id", referencedColumnName = "id")

    )
    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<User> followedUsers = new HashSet<>();

    @ManyToMany(mappedBy = "userFavorites")
    private Set<Article> favoritedArticles = new HashSet<>();

    protected User() {}

    private User(String username, String email, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
    }

    public static User createUser(String username, String email, String encodedPassword) {
        return new User(username, email, encodedPassword);
    }

    public static User fromRegistrationRequest(UserRegistrationRequest registrationRequest, PasswordEncoder passwordEncoder) {
        return new User(registrationRequest.getUsername(), registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()));
    }

    public void encryptAndSetPassword(String rawPassword, PasswordEncoder passwordEncoder) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFollowing(User user) {
        return this.followedUsers.contains(user);
    }

    public Set<User> getFollowedUsers() {
        return followedUsers;
    }

    public User followUser(User user) {
        followedUsers.add(user);
        return this;
    }

    public User unfollowUser(User user) {
        followedUsers.remove(user);
        return user;
    }

    public Article favoriteArticle(Article article) {
        favoritedArticles.add(article);
        return article.addUserToFavorites(this);
    }

    public Article unfavoriteArticle(Article article) {
        favoritedArticles.remove(article);
        return article.removeUserFromFavorites(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
