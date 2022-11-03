package io.github.hossensyedriadh.restdemo.service.user;

import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.exception.ResourceException;
import io.github.hossensyedriadh.restdemo.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           HttpServletRequest httpServletRequest) {
        this.userRepository = userRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public Page<User> users(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public List<User> users() {
        return this.userRepository.findAll();
    }

    @Override
    public Optional<User> user(String username) {
        return this.userRepository.findById(username);
    }

    @Override
    public User update(User user) {
        if (this.userRepository.findById(user.getUsername()).isPresent()) {
            return this.userRepository.saveAndFlush(user);
        }
        throw new ResourceException("User not found with username: " + user.getUsername(),
                HttpStatus.BAD_REQUEST, httpServletRequest);
    }
}
