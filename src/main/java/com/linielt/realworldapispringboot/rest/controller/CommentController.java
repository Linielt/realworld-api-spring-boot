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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<CommentDto> addCommentToArticle(JwtAuthenticationToken jwtToken,
                                          @PathVariable String slug,
                                          @RequestBody CommentCreationRequest creationRequest) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        return new ResponseEntity<>(CommentDto.fromComment(commentService.addCommentToArticle(currentUser, article, creationRequest)),
                HttpStatus.OK);
    }

    @GetMapping("/articles/{slug}/comments")
    public ResponseEntity<ArticleCommentsResponse> getCommentsFromArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        Article article = null;
        try {
            article = articleService.getArticleFromSlug(slug);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Comment> articleComments = commentService.getCommentsFromArticle(article);

        if (jwtToken == null) {
            return new ResponseEntity<>(new ArticleCommentsResponse(articleComments.stream()
                    .map(NestedCommentDto::fromComment)
                    .toList()),
                    HttpStatus.OK);
        }

        User currentUser = userService.getUserByToken(jwtToken);

        return new ResponseEntity<>(new ArticleCommentsResponse(articleComments.stream()
                .map(comment -> NestedCommentDto.fromCommentAndCurrentUser(comment, currentUser))
                .toList()),
                HttpStatus.OK);
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    public ResponseEntity<Void> deleteComment(JwtAuthenticationToken jwtToken, @PathVariable String slug, @PathVariable int id) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);
        Comment comment = commentService.getCommentFromId(id);

        commentService.deleteComment(currentUser, article, comment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
