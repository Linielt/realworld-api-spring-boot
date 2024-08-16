package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.CommentDto;
import com.linielt.realworldapispringboot.dtos.CommentDto.NestedCommentDto;
import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.Comment;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.request.CommentCreationRequest;
import com.linielt.realworldapispringboot.response.ArticleCommentsResponse;
import com.linielt.realworldapispringboot.service.ArticleService;
import com.linielt.realworldapispringboot.service.CommentService;
import com.linielt.realworldapispringboot.service.UserService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final ArticleService articleService;
    private final UserService userService;
    private final CommentService commentService;

    public CommentController(ArticleService articleService, UserService userService, CommentService commentService) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("/articles/{slug}/comments")
    public CommentDto addCommentToArticle(JwtAuthenticationToken jwtToken,
                                          @PathVariable String slug,
                                          @RequestBody CommentCreationRequest creationRequest) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        return CommentDto.fromComment(commentService.addCommentToArticle(currentUser, article, creationRequest));
    }

    @GetMapping("/articles/{slug}/comments")
    public ArticleCommentsResponse getCommentsFromArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        Article article = articleService.getArticleFromSlug(slug);
        List<Comment> articleComments = commentService.getCommentsFromArticle(article);

        if (jwtToken == null) {
            return new ArticleCommentsResponse(articleComments.stream()
                    .map(NestedCommentDto::fromComment)
                    .toList());
        }

        User currentUser = userService.getUserByToken(jwtToken);

        return new ArticleCommentsResponse(articleComments.stream()
                .map(comment -> NestedCommentDto.fromCommentAndCurrentUser(comment, currentUser))
                .toList());
    }
}
