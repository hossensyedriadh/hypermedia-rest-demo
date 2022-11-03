package io.github.hossensyedriadh.restdemo.service.comment;

import io.github.hossensyedriadh.restdemo.entity.Comment;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
import io.github.hossensyedriadh.restdemo.exception.ResourceException;
import io.github.hossensyedriadh.restdemo.repository.jpa.CommentRepository;
import io.github.hossensyedriadh.restdemo.repository.jpa.PostRepository;
import io.github.hossensyedriadh.restdemo.service.CurrentAuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CurrentAuthenticationContext currentAuthenticationContext;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public CommentServiceImpl(PostRepository postRepository,
                              CommentRepository commentRepository, CurrentAuthenticationContext currentAuthenticationContext,
                              HttpServletRequest httpServletRequest) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.currentAuthenticationContext = currentAuthenticationContext;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public List<Comment> comments(String postId) {
        return this.commentRepository.findCommentsOnPost(postId);
    }

    @Override
    public Optional<Comment> comment(String id) {
        return this.commentRepository.findById(id);
    }

    @Override
    public Comment create(Comment comment) {
        if (postRepository.findById(comment.getPost().getId()).isPresent()) {
            return this.commentRepository.saveAndFlush(comment);
        }

        throw new ResourceException("Post doesn't exist with ID: " + comment.getPost().getId(),
                HttpStatus.BAD_REQUEST, httpServletRequest);
    }

    @Override
    public Comment update(Comment comment) {
        if (this.commentRepository.findById(comment.getId()).isPresent()) {
            if (this.commentRepository.findById(comment.getId()).get().getCommentBy().getUsername()
                    .equals(this.currentAuthenticationContext.getAuthenticationPrincipal())) {
                this.commentRepository.saveAndFlush(comment);
                return this.commentRepository.findById(comment.getId()).get();
            } else {
                throw new ResourceException("You are not authorized to perform this action", HttpStatus.FORBIDDEN, httpServletRequest);
            }
        }

        throw new ResourceException("Comment not found with ID: " + comment.getId(), HttpStatus.BAD_REQUEST, httpServletRequest);
    }

    @Override
    public void delete(String id) {
        if (this.commentRepository.findById(id).isPresent()) {
            if (this.commentRepository.findById(id).get().getCommentBy().getUsername().equals(this.currentAuthenticationContext.getAuthenticationPrincipal())
                    || this.currentAuthenticationContext.getPrincipalRole().equals(Authority.ROLE_ADMINISTRATOR.toString())) {
                this.commentRepository.deleteById(id);
            } else {
                throw new ResourceException("You are not authorized to perform this action", HttpStatus.FORBIDDEN, httpServletRequest);
            }
        } else {
            throw new ResourceException("Comment not found with ID: " + id, HttpStatus.BAD_REQUEST, httpServletRequest);
        }
    }
}
