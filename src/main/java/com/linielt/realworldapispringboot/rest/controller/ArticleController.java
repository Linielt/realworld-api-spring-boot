package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.ArticleDto;
import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
import com.linielt.realworldapispringboot.request.ArticleEditRequest;
import com.linielt.realworldapispringboot.response.ListOfArticlesResponse;
import com.linielt.realworldapispringboot.service.ArticleService;
import com.linielt.realworldapispringboot.service.UserService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/articles")
    public ListOfArticlesResponse getListOfArticles(JwtAuthenticationToken jwtToken,
                                                    @RequestParam(required = false) String tag,
                                                    @RequestParam(required = false) String author,
                                                    @RequestParam(required = false) String user,
                                                    @RequestParam(required = false, defaultValue = "20") int limit,
                                                    @RequestParam(required = false, defaultValue = "0") int offset) {
        if (jwtToken == null) {
            return new ListOfArticlesResponse(
                    articleService.getListOfArticlesFromParameters(tag, author, user, limit, offset).stream()
                            .map(ArticleDto::fromArticleToDto)
                            .toList()
            );
        }

        User currentUser = userService.getUserByToken(jwtToken);

        return new ListOfArticlesResponse(
                articleService.getListOfArticlesFromParameters(tag, author, user, limit, offset).stream()
                        .map(article -> ArticleDto.fromArticleAndCurrentUserToDto(article, currentUser))
                        .toList()
        );
    }

    @GetMapping("/articles/feed")
    public ListOfArticlesResponse getArticlesFeed(JwtAuthenticationToken jwtToken,
                                            @RequestParam(required = false, defaultValue = "20") int limit,
                                            @RequestParam(required = false, defaultValue = "0") int offset) {
        User currentUser = userService.getUserByToken(jwtToken);

        return new ListOfArticlesResponse(
                articleService.getArticlesFeed(currentUser, limit, offset).stream()
                        .map(article -> ArticleDto.fromArticleAndCurrentUserToDto(article, currentUser))
                        .toList()
        );
    }

    @GetMapping("/articles/{slug}")
    public ArticleDto getArticleFromSlug(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        if (jwtToken == null) {
            return ArticleDto.fromArticleToDto(articleService.getArticleFromSlug(slug));
        }

        return ArticleDto.fromArticleAndCurrentUserToDto(articleService.getArticleFromSlug(slug), userService.getUserByToken(jwtToken));
    }

    @PostMapping("/articles")
    public ArticleDto createArticle(JwtAuthenticationToken jwtToken, @RequestBody ArticleCreationRequest creationRequest) {
        User author = userService.getUserByToken(jwtToken);

        return ArticleDto.fromArticleToDto(articleService.createArticle(author, creationRequest));
    }

    @PutMapping("/articles/{slug}")
    public ArticleDto editArticle(JwtAuthenticationToken jwtToken,
                                  @PathVariable String slug,
                                  @RequestBody ArticleEditRequest editRequest) {
        User currentUser = userService.getUserByToken(jwtToken);

        return ArticleDto.fromArticleAndCurrentUserToDto(
                articleService.editArticle(currentUser, slug, editRequest),
                currentUser
        );
    }

    @DeleteMapping("/articles/{slug}")
    public void deleteArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        articleService.deleteArticle(currentUser, article);
    }

    @PostMapping("/articles/{slug}/favorite")
    public ArticleDto favoriteArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        return ArticleDto.fromArticleAndCurrentUserToDto(articleService.favoriteArticle(currentUser, article), currentUser);
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ArticleDto unfavoriteArticle(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        User currentUser = userService.getUserByToken(jwtToken);
        Article article = articleService.getArticleFromSlug(slug);

        return ArticleDto.fromArticleToDto(articleService.unfavoriteArticle(currentUser, article));
    }
}
