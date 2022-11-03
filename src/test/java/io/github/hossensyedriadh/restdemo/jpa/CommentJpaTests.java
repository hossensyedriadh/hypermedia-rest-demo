package io.github.hossensyedriadh.restdemo.jpa;

import io.github.hossensyedriadh.restdemo.entity.Comment;
import io.github.hossensyedriadh.restdemo.entity.Post;
import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
import io.github.hossensyedriadh.restdemo.repository.jpa.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
@WebAppConfiguration
@SpringBootTest
public class CommentJpaTests {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Transactional
    @WithMockUser
    public void should_persist_comment() {
        User user = this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));

        Post post = this.testEntityManager.persistAndFlush(new Post(UUID.randomUUID().toString(), "some post",
                LocalDateTime.now(), user, null, new LinkedHashSet<>()));

        Comment comment = new Comment(UUID.randomUUID().toString(), "some comment", LocalDateTime.now(), post,
                user, null);

        String id = this.testEntityManager.persistAndGetId(comment, String.class);

        Optional<Comment> commentOptional = this.commentRepository.findById(id);

        assert commentOptional.isPresent();
    }

    @Test
    @Transactional
    @WithMockUser
    public void should_update_comment() {
        User user = this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));

        Post post = this.testEntityManager.persistAndFlush(new Post(UUID.randomUUID().toString(), "some post",
                LocalDateTime.now(), user, null, new LinkedHashSet<>()));

        Comment comment = new Comment(UUID.randomUUID().toString(), "some comment", LocalDateTime.now(),
                post, user, null);

        String id = this.testEntityManager.persistAndGetId(comment, String.class);

        Comment found = this.commentRepository.findById(id).get();
        found.setContent("some updated comment");

        this.testEntityManager.persistAndFlush(found);

        Comment updated = this.commentRepository.findById(id).get();

        assert updated.getContent().equals("some updated comment") && updated.getLastModifiedOn().isBefore(LocalDateTime.now());
    }

    @Test
    @Transactional
    @WithMockUser
    public void should_delete_comment() {
        User user = this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));

        Post post = this.testEntityManager.persistAndFlush(new Post(UUID.randomUUID().toString(), "some post",
                LocalDateTime.now(), user, null, new LinkedHashSet<>()));

        Comment comment = new Comment(UUID.randomUUID().toString(), "some comment", LocalDateTime.now(),
                post, user, null);

        String id = this.testEntityManager.persistAndGetId(comment, String.class);

        this.commentRepository.findById(id).ifPresent(value -> this.commentRepository.delete(value));

        assert this.commentRepository.findById(id).isEmpty();
    }
}
