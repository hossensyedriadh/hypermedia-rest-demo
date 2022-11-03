package io.github.hossensyedriadh.restdemo.service.post;

import io.github.hossensyedriadh.restdemo.entity.Post;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
import io.github.hossensyedriadh.restdemo.exception.ResourceException;
import io.github.hossensyedriadh.restdemo.repository.jpa.PostRepository;
import io.github.hossensyedriadh.restdemo.service.CurrentAuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CurrentAuthenticationContext currentAuthenticationContext;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CurrentAuthenticationContext currentAuthenticationContext,
                           HttpServletRequest httpServletRequest) {
        this.postRepository = postRepository;
        this.currentAuthenticationContext = currentAuthenticationContext;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public Page<Post> posts(Pageable pageable) {
        return this.postRepository.findAll(pageable);
    }

    @Override
    public List<Post> posts() {
        return this.postRepository.findAll();
    }

    @Override
    public Page<Post> posts(Pageable pageable, String username) {
        return this.postRepository.findPostsByPostedBy(username, pageable);
    }

    @Override
    public List<Post> posts(String username) {
        return this.postRepository.findPostsByPostedBy(username);
    }

    @Override
    public Optional<Post> post(String id) {
        return this.postRepository.findById(id);
    }

    @Override
    public Post create(Post post) {
        return this.postRepository.saveAndFlush(post);
    }

    @Override
    public Post update(Post post) {
        if (this.postRepository.findById(post.getId()).isPresent()) {
            if (this.postRepository.findById(post.getId()).get().getPostedBy().getUsername()
                    .equals(this.currentAuthenticationContext.getAuthenticationPrincipal())) {
                this.postRepository.saveAndFlush(post);
                return this.postRepository.findById(post.getId()).get();
            } else {
                throw new ResourceException("You are not authorized to perform this action", HttpStatus.FORBIDDEN, httpServletRequest);
            }
        }
        throw new ResourceException("Post not found with ID: " + post.getId(), HttpStatus.BAD_REQUEST, httpServletRequest);
    }

    @Override
    public void delete(String id) {
        if (this.postRepository.findById(id).isPresent()) {
            if (this.postRepository.findById(id).get().getPostedBy().getUsername()
                    .equals(this.currentAuthenticationContext.getAuthenticationPrincipal())
                    || this.currentAuthenticationContext.getPrincipalRole().equals(Authority.ROLE_ADMINISTRATOR.toString())) {
                this.postRepository.deleteById(id);
            } else {
                throw new ResourceException("You are not authorized to perform this action", HttpStatus.FORBIDDEN, httpServletRequest);
            }
        } else {
            throw new ResourceException("Post not found with ID: " + id, HttpStatus.BAD_REQUEST, httpServletRequest);
        }
    }
}
