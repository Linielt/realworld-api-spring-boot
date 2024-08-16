package com.linielt.realworldapispringboot.response;

import com.linielt.realworldapispringboot.dtos.CommentDto.NestedCommentDto;

import java.util.List;

public class ArticleCommentsResponse {
    private final List<NestedCommentDto> comments;

    public ArticleCommentsResponse(List<NestedCommentDto> comments) {
        this.comments = comments;
    }

    public List<NestedCommentDto> getComments() {
        return comments;
    }
}
