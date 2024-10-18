package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.ArticleDto;
import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
import com.linielt.realworldapispringboot.request.ArticleEditRequest;
import com.linielt.realworldapispringboot.response.ListOfArticlesResponse;
import com.linielt.realworldapispringboot.service.ArticleService;
import com.linielt.realworldapispringboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Validated
@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/articles")
    public ResponseEntity<ListOfArticlesResponse> getListOfArticles(JwtAuthenticationToken jwtToken,
                                                    @RequestParam(required = false) String tag,
                                                    @RequestParam(required = false) String author,
                                                    @RequestParam(required = false) String user,
                                                    @RequestParam(required = false, defaultValue = "20") int limit,
                                                    @RequestParam(required = false, defaultValue = "0") int offset) {
        if (jwtToken == null) {
            return new ResponseEntity<>(new ListOfArticlesResponse(
                    articleService.getListOfArticlesFromParameters(tag, author, user, limit, offset).stream()
                            .map(ArticleDto::fromArticleToDto)
                            .toList()),
                    HttpStatus.OK);
        }

        User currentUser = userService.getUserByToken(jwtToken);

        return new ResponseEntity<>(new ListOfArticlesResponse(
                articleService.getListOfArticlesFromParameters(tag, author, user, limit, offset).stream()
                        .map(article -> ArticleDto.fromArticleAndCurrentUserToDto(article, currentUser))
                        .toList()),
                HttpStatus.OK);
    }

    @GetMapping("/articles/feed")
    public ResponseEntity<ListOfArticlesResponse> getArticlesFeed(JwtAuthenticationToken jwtToken,
                                            @RequestParam(required = false, defaultValue = "20") int limit,
                                            @RequestParam(required = false, defaultValue = "0") int offset) {
        if (jwtToken == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // For some reason, the method below still runs even when unauthorized so I have to do this weirdness.
        User currentUser = userService.getUserByToken(jwtToken);

        return new ResponseEntity<>(new ListOfArticlesResponse(
                articleService.getArticlesFeed(currentUser, limit, offset).stream()
                        .map(article -> ArticleDto.fromArticleAndCurrentUserToDto(article, currentUser))
                        .toList()),
                HttpStatus.OK);
    }

    @GetMapping("/articles/{slug}")
    public ResponseEntity<ArticleDto> getArticleFromSlug(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        try {
            if (jwtToken == null) {
                return new ResponseEntity<>(ArticleDto.fromArticleToDto(articleService.getArticleFromSlug(slug)),
                        HttpStatus.OK);
            }

            return new ResponseEntity<>(ArticleDto.fromArticleAndCurrentUserToDto(
                    articleService.getArticleFromSlug(slug),
                    userService.getUserByToken(jwtToken)),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleDto> createArticle(JwtAuthenticationToken jwtToken, @RequestBody @Valid ArticleCreationRequest creationRequest) {
        User author = userService.getUserByToken(jwtToken);

        return new ResponseEntity<>(ArticleDto.fromArticleToDto(articleService.createArticle(author, creationRequest)),
                HttpStatus.OK);
    }

    @PutMapping("/articles/{slug}")
    public ResponseEntity<ArticleDto> editArticle(JwtAuthenticationToken jwtToken,
                                  @PathVariable String slug,
                                  @RequestBody ArticleEditRequest editRequest) {
        User currentUser = userService.getUserByToken(jwtToken);

        try {
            return new ResponseEntity<>(ArticleDto.fromArticleAndCurrentUserToDto(
                    articleService.editArticle(currentUser, slug, editRequest),
                    currentUser),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e1) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e2) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/articles/{slug}")
    public ResponseEntity<Void> deleteArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        try {
            articleService.deleteArticle(currentUser, article);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // I hope I am handling this right.
        }
    }

    @PostMapping("/articles/{slug}/favorite")
    public ResponseEntity<ArticleDto> favoriteArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        return new ResponseEntity<>(ArticleDto.fromArticleAndCurrentUserToDto(
                articleService.favoriteArticle(currentUser, article), currentUser),
                HttpStatus.OK);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ResponseEntity<ArticleDto> unfavoriteArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        return new ResponseEntity<>(ArticleDto.fromArticleToDto(articleService.unfavoriteArticle(currentUser, article)),
                HttpStatus.OK);
    }
}
