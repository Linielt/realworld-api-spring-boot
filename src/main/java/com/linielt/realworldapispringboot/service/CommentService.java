package com.linielt.realworldapispringboot.service;

import com.linielt.realworldapispringboot.model.Article;
import com.linielt.realworldapispringboot.model.Comment;
import com.linielt.realworldapispringboot.model.User;
import com.linielt.realworldapispringboot.repository.CommentRepository;
import com.linielt.realworldapispringboot.request.CommentCreationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
