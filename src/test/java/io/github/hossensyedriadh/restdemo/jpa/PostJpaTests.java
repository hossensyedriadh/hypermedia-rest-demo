package io.github.hossensyedriadh.restdemo.jpa;

import io.github.hossensyedriadh.restdemo.entity.Post;
import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
import io.github.hossensyedriadh.restdemo.repository.jpa.PostRepository;
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

import java.time.Clock;
import java.time.Instant;
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
public class PostJpaTests {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    @WithMockUser
    public void should_persist_post() {
        User user = this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));

        String pid = this.testEntityManager.persistAndGetId(new Post(UUID.randomUUID().toString(), "some content", Instant.now(Clock.systemDefaultZone()).getEpochSecond(),
                user, null, new LinkedHashSet<>()), String.class);

        assert this.postRepository.findById(pid).isPresent();
    }

    @Test
    @Transactional
    @WithMockUser
    public void should_update_post() {
        User user = this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));

        String id = this.testEntityManager.persistAndGetId(new Post(UUID.randomUUID().toString(), "some content", Instant.now(Clock.systemDefaultZone()).getEpochSecond(),
                user, null, new LinkedHashSet<>()), String.class);

        Post post = this.postRepository.findById(id).get();
        post.setContent("some updated content");

        this.testEntityManager.persistAndFlush(post);

        Post updated = this.postRepository.findById(id).get();

        assert updated.getContent().equals("some updated content");
    }

    @Test
    @Transactional
    @WithMockUser
    public void should_delete_post() {
        User user = this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));

        String id = this.testEntityManager.persistAndGetId(new Post(UUID.randomUUID().toString(), "some content", Instant.now(Clock.systemDefaultZone()).getEpochSecond(),
                user, null, new LinkedHashSet<>()), String.class);

        Optional<Post> post = this.postRepository.findById(id);
        post.ifPresent(value -> this.postRepository.delete(value));

        assert this.postRepository.findById(id).isEmpty();
    }
}
