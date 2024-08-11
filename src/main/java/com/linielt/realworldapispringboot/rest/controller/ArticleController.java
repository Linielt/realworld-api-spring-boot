package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.ArticleDto;
import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
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

    @GetMapping("/articles/{slug}")
    public ArticleDto getArticleFromSlug(JwtAuthenticationToken jwtToken, @PathVariable String slug) {
        Article article = articleService.getArticleFromSlug(slug);
        ArticleDto dto = ArticleDto.fromArticleToDto(article);
        if (jwtToken != null) {
            dto.setFavorited(article.getFavorites().contains(userService.getUserByToken(jwtToken)));
        }

        return dto;
    }

    @PostMapping("/articles")
    public ArticleDto createArticle(JwtAuthenticationToken jwtToken, @RequestBody ArticleCreationRequest creationRequest) {
        return ArticleDto.fromArticleToDto(articleService.createArticle(jwtToken, creationRequest));
    }
}
