package com.linielt.realworldapispringboot.response;

import com.linielt.realworldapispringboot.dtos.ArticleDto;

import java.util.List;

public class ListOfArticlesResponse {

    private final List<ArticleDto> articles;

    public ListOfArticlesResponse(List<ArticleDto> articles) {
        this.articles = articles;
    }

    public List<ArticleDto> getArticles() {
        return articles;
    }
}
