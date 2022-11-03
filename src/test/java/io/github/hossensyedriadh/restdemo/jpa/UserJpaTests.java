package io.github.hossensyedriadh.restdemo.jpa;

import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
import io.github.hossensyedriadh.restdemo.repository.jpa.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
@WebAppConfiguration
@SpringBootTest
public class UserJpaTests {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void should_persist_user() {
        this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));
        Optional<User> user = this.userRepository.findById("test");

        assert user.isPresent();
    }

    @Test
    @Transactional
    public void should_update_user() {
        this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));

        User user = this.userRepository.findById("test").get();
        user.setAccountNotLocked(false);

        this.testEntityManager.persistAndFlush(user);

        User updated = this.userRepository.findById("test").get();

        assert !updated.isAccountNotLocked();
    }

    @Test
    @Transactional
    public void should_delete_user() {
        this.testEntityManager.persistAndFlush(new User("test", "password", true, Authority.ROLE_USER));
        Optional<User> user = this.userRepository.findById("test");
        user.ifPresent(value -> this.userRepository.delete(value));

        assert this.userRepository.findById("test").isEmpty();
    }
}
