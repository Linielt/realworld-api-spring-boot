package com.linielt.realworldapispringboot.service;

import com.github.slugify.Slugify;
import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.repository.ArticleRepository;
import com.linielt.realworldapispringboot.request.ArticleCreationRequest;
import com.linielt.realworldapispringboot.request.ArticleEditRequest;
import com.linielt.realworldapispringboot.request.OffsetBasedPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {
    private final TagService tagService; // TODO - Consider how to remove this dependency
    private final ArticleRepository articleRepository;

    public ArticleService(TagService tagService, ArticleRepository articleRepository) {
        this.tagService = tagService;
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> getListOfArticlesFromParameters(String tag, String author, String userFavorited, int limit, int offset) {
        Pageable sortedByCreationTime = new OffsetBasedPageRequest(offset, limit, Sort.by("createdAt").descending());

        List<Article> articles = articleRepository.findAll(sortedByCreationTime).stream().toList();

        if (tag != null) {
            articles = articles.stream().filter(article -> article.getTagsAsStringList().contains(tag)).toList();
        }
        if (author != null) {
            articles = articles.stream().filter(article -> article.getAuthor().getUsername().equals(author)).toList();
        }
        if (userFavorited != null) {
            articles = articles.stream().filter(article -> article.getUsernamesOfFavorites().contains(userFavorited)).toList();
        }

        return articles;
    }

    @Transactional(readOnly = true)
    public List<Article> getArticlesFeed(User currentUser, int limit, int offset) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.by("createdAt").descending());

        return articleRepository.findAllByAuthorIsIn(currentUser.getFollowedUsers(),pageable).stream()
                .filter(article -> currentUser.isFollowing(article.getAuthor()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Article getArticleFromSlug(String slug) {
        return articleRepository.findArticleBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Article not found."));
    }

    @Transactional
    public Article createArticle(User author, ArticleCreationRequest creationRequest) {
        Article article = Article.fromCreationRequest(creationRequest, author);

        if (creationRequest.tagList() != null)
            article.setTags(tagService.loadTagsIfPresent(creationRequest.tagList()));

        return articleRepository.save(article);
    }

    @Transactional
    public Article editArticle(User currentUser, String slug, ArticleEditRequest editRequest) {
        Article articleToEdit = articleRepository.findArticleBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Article not found."));

        final Slugify slg = Slugify.builder().build();

        if (articleToEdit.getAuthor() != currentUser) {
            throw new IllegalArgumentException("You cannot edit the articles of other users.");
        }

        if (editRequest.title() != null) {
            articleToEdit.setTitle(editRequest.title());
            articleToEdit.setSlug(slg.slugify(editRequest.title()));
        }
        if (editRequest.description() != null) {
            articleToEdit.setDescription(editRequest.description());
        }
        if (editRequest.body() != null) {
            articleToEdit.setBody(editRequest.body());
        }

        return articleRepository.save(articleToEdit);
    }

    @Transactional
    public void deleteArticle(User currentUser, Article articleToDelete) {
        if (articleToDelete.getAuthor().equals(currentUser)) {
            throw new IllegalArgumentException("You cannot delete the articles of other users.");
        }

        articleRepository.delete(articleToDelete);
    }

    @Transactional
    public Article favoriteArticle(User currentUser, Article article) {
        return articleRepository.save(currentUser.favoriteArticle(article));
    }

    @Transactional
    public Article unfavoriteArticle(User currentUser, Article article) {
        return articleRepository.save(currentUser.unfavoriteArticle(article));
    }
}
