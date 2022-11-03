package io.github.hossensyedriadh.restdemo.service.comment;

import io.github.hossensyedriadh.restdemo.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> comments(String postId);

    Optional<Comment> comment(String id);

    Comment create(Comment comment);

    Comment update(Comment comment);

    void delete(String id);
}
