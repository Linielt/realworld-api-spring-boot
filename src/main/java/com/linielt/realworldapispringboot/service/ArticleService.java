package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.repository.ArticleRepository;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ArticleService {

    private final UserService userService;
    private final TagService tagService;
    private final ArticleRepository articleRepository;

    public ArticleService(UserService userService, TagService tagService, ArticleRepository articleRepository) {
        this.userService = userService;
        this.tagService = tagService;
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public Article getArticleFromSlug(String slug) {
        return articleRepository.findArticleBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Article not found."));
    }

    @Transactional
    public Article createArticle(JwtAuthenticationToken jwtToken, ArticleCreationRequest creationRequest) {
        Article article = Article.fromCreationRequest(creationRequest, userService.getUserByToken(jwtToken));

        if (creationRequest.tagList() != null)
            article.setTags(tagService.loadTagsIfPresent(creationRequest.tagList()));

        return articleRepository.save(article);
    }
}
