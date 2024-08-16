package com.linielt.realworldapispringboot.dtos;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linielt.realworldapispringboot.dtos.ProfileDto.NestedProfileDto;
import com.linielt.realworldapispringboot.model.Comment;
import com.linielt.realworldapispringboot.model.User;

import java.time.Instant;

@JsonTypeName("comment")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class CommentDto {

    private int id;
    private Instant createdAt;
    private Instant updatedAt;
    private String body;
    private NestedProfileDto author;

    protected CommentDto() {}

    private CommentDto(Comment comment) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
        this.author = NestedProfileDto.fromUser(comment.getAuthor());
    }

    public static CommentDto fromComment(Comment comment) {
        return new CommentDto(comment);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public NestedProfileDto getAuthor() {
        return author;
    }

    public void setAuthor(NestedProfileDto author) {
        this.author = author;
    }

    public static class NestedCommentDto {
        private int id;
        private Instant createdAt;
        private Instant updatedAt;
        private String body;
        private NestedProfileDto author;

        protected NestedCommentDto() {}

        private NestedCommentDto(Comment comment) {
            this.id = comment.getId();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.body = comment.getBody();
            this.author = NestedProfileDto.fromUser(comment.getAuthor());
        }

        public static NestedCommentDto fromComment(Comment comment) {
            return new NestedCommentDto(comment);
        }

        public static NestedCommentDto fromCommentAndCurrentUser(Comment comment, User currentUser) {
            NestedCommentDto dto = new NestedCommentDto(comment);

            dto.author.setFollowing(currentUser.isFollowing(comment.getAuthor()));

            return dto;
        }

        public int getId() {
            return id;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public String getBody() {
            return body;
        }

        public NestedProfileDto getAuthor() {
            return author;
        }
    }
}
