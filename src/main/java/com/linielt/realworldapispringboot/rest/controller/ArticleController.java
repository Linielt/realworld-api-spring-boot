package com.linielt.realworldapispringboot.rest.controller;

import com.linielt.realworldapispringboot.dtos.ArticleDto;
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
        if (jwtToken == null) {
            return ArticleDto.fromArticleToDto(articleService.getArticleFromSlug(slug));
        }

        return ArticleDto.fromArticleAndCurrentUserToDto(articleService.getArticleFromSlug(slug), userService.getUserByToken(jwtToken));
    }

    @PostMapping("/articles")
    public ArticleDto createArticle(JwtAuthenticationToken jwtToken, @RequestBody ArticleCreationRequest creationRequest) {
        return ArticleDto.fromArticleToDto(articleService.createArticle(jwtToken, creationRequest));
    }
}
