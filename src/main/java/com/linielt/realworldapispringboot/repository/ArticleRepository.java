package com.linielt.realworldapispringboot.repository;

import com.linielt.realworldapispringboot.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findArticleBySlug(String slug);
}
