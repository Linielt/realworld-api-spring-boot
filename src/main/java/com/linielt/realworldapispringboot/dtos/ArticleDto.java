package com.linielt.realworldapispringboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.dtos.ProfileDto.NestedProfileDto;
import com.linielt.realworldapispringboot.model.User;

import java.time.Instant;
import java.util.List;

@JsonTypeName("author")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class ArticleDto {

    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean favorited;
    private int favoritesCount;
    @JsonProperty("author")
    private NestedProfileDto author;

    public static ArticleDto fromArticleToDto(Article article) {
        ArticleDto dto = new ArticleDto(article.getSlug(), article.getTitle(), article.getDescription(),
                article.getBody(),
                article.getTagsAsStringList(),
                article.getCreatedAt(),
                article.getUpdatedAt());
        dto.setFavoritesCount(article.getUserFavorites().size());
        dto.setAuthor(NestedProfileDto.fromProfileDTO(ProfileDto.fromUser(article.getAuthor())));

        return dto;
    }

    public static ArticleDto fromArticleAndCurrentUserToDto(Article article, User currentUser) {
        ArticleDto dto = new ArticleDto(article.getSlug(), article.getTitle(), article.getDescription(),
                article.getBody(),
                article.getTagsAsStringList(),
                article.getCreatedAt(),
                article.getUpdatedAt());
        dto.setFavorited(article.getUserFavorites().contains(currentUser));
        dto.setFavoritesCount(article.getUserFavorites().size());
        dto.setAuthor(NestedProfileDto.fromProfileDTO(
                ProfileDto.fromUser(article.getAuthor()),
                currentUser.isFollowing(article.getAuthor())
        )
        );

        return dto;
    }

    private ArticleDto(String slug, String title, String description, String body, List<String> tagList, Instant createdAt, Instant updatedAt) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
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

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public NestedProfileDto getAuthor() {
        return author;
    }

    public void setAuthor(NestedProfileDto author) {
        this.author = author;
    }
}
