package com.linielt.realworldapispringboot.repository;

import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> getCommentsByArticle(Article article);
    Optional<Comment> getCommentById(int id);
}
