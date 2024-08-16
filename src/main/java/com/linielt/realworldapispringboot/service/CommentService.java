package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.Comment;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.repository.CommentRepository;
import com.linielt.realworldapispringboot.request.CommentCreationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService {
    private final CommentRepository commentRepository;


    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment addCommentToArticle(User currentUser, Article article, CommentCreationRequest creationRequest) {
        return commentRepository.save
                (Comment.fromCreationRequestAndUserAndArticle(creationRequest, currentUser, article));
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsFromArticle(Article article) {
        return commentRepository.getCommentsByArticle(article);
    }

    @Transactional(readOnly = true)
    public Comment getCommentFromId(int id) {
        return commentRepository.getCommentById(id)
                .orElseThrow(() -> new NoSuchElementException("A comment with this id does not exist."));
    }

    @Transactional
    public void deleteComment(User currentUser, Article article, Comment comment) {
        if (!currentUser.equals(comment.getAuthor())) {
            throw new IllegalArgumentException("You cannot delete another user's comments.");
        }
        if (!article.equals(comment.getArticle())) {
            throw new IllegalArgumentException("You must specify the correct slug of the article the comment belongs to.");
        }

        commentRepository.delete(comment);
    }
}
