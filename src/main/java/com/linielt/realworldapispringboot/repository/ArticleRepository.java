package com.linielt.realworldapispringboot.repository;

import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findArticleBySlug(String slug);
    List<Article> findAllByAuthorIsIn(Set<User> followedUsers, Pageable pageable);
}
