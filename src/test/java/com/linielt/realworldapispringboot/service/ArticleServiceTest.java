package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.Tag;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.repository.ArticleRepository;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ArticleServiceTest {

    @Mock
    TagService tagService;
    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    ArticleService articleService;

    private Article testArticle;

    @BeforeEach
    void setup() {

        List<String> tagsList = new ArrayList<>();
        tagsList.add("tag1");
        tagsList.add("tag2");

        testArticle = Article.fromCreationRequest
                (new ArticleCreationRequest("test", "test", "test", tagsList)
                        ,User.createUser("testAuthor", "testAuthor", "testAuthor"));
    }
}
