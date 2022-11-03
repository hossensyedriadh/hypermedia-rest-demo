package io.github.hossensyedriadh.restdemo.service.post;

import io.github.hossensyedriadh.restdemo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Page<Post> posts(Pageable pageable);

    List<Post> posts();

    Page<Post> posts(Pageable pageable, String username);

    List<Post> posts(String username);

    Optional<Post> post(String id);

    Post create(Post post);

    Post update(Post post);

    void delete(String id);
}
