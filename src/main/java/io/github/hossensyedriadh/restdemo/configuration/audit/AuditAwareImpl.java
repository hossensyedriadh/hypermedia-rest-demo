package io.github.hossensyedriadh.restdemo.configuration.audit;

import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditAwareImpl implements AuditorAware<User> {
    private final UserRepository userRepository;

    @Autowired
    public AuditAwareImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    @NonNull
    public Optional<User> getCurrentAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return this.userRepository.findById(username);
    }
}
