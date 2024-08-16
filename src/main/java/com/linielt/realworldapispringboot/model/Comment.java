package com.linielt.realworldapispringboot.model;

import com.linielt.realworldapispringboot.request.CommentCreationRequest;
import jakarta.persistence.*;

import java.time.Instant;

@Table(name = "comments")
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private User author;

    @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Article article;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
    private String body;

    protected Comment() {}
    private Comment(String body, User author, Article article) {
        this.body = body;
        this.author = author;
        this.article = article;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static Comment fromCreationRequestAndUserAndArticle
            (CommentCreationRequest creationRequest, User author, Article article) {
        return new Comment(creationRequest.body(), author, article);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
