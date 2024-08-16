package com.linielt.realworldapispringboot.model;

import com.github.slugify.Slugify;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.*;

@Table(name = "articles")
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String slug;
    private String title;
    private String description;
    private String body;

    @JoinTable(
            name = "articles_tags",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @JoinTable(
            name = "article_favorites",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @ManyToMany(cascade = CascadeType.REMOVE)
    private Set<User> userFavorites = new HashSet<>();

    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @ManyToOne
    private User author;

    public static Article fromCreationRequest(ArticleCreationRequest creationRequest, User author) {
        Article article = new Article(creationRequest.title(), creationRequest.description(), creationRequest.body(), author);

        final Slugify slg = Slugify.builder().build();
        article.slug = slg.slugify(creationRequest.title());

        article.createdAt = Instant.now();
        article.updatedAt = Instant.now();

        return article;
    }

    protected Article() {}

    private Article(String title, String description, String body, User author) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public List<String> getTagsAsStringList() {
        List<String> tagsAsStrings = new ArrayList<>();

        for (Tag tag : this.tags) {
            tagsAsStrings.add(tag.getValue());
        }

        return tagsAsStrings;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<User> getUserFavorites() {
        return userFavorites;
    }
    public List<String> getUsernamesOfFavorites() {
        List<String> usernames = new ArrayList<>();

        for (User user : this.userFavorites) {
            usernames.add(user.getUsername());
        }

        return usernames;
    }

    public void setUserFavorites(Set<User> userFavorites) {
        this.userFavorites = userFavorites;
    }

    public Article addUserToFavorites(User user) {
        this.userFavorites.add(user);
        return this;
    }

    public Article removeUserFromFavorites(User user) {
        this.userFavorites.remove(user);
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
