package io.github.hossensyedriadh.restdemo.configuration.authentication.service;

import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.exception.UserAccountLockedException;
import io.github.hossensyedriadh.restdemo.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

@Service
public class BearerAuthenticationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public BearerAuthenticationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may be case-sensitive, or case-insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (this.userRepository.findById(username).isPresent()) {
            User user = this.userRepository.findById(username).get();

            UserDetails userDetails = new UserDetails() {
                @Serial
                private static final long serialVersionUID = 1828493362715627689L;

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.singletonList(new SimpleGrantedAuthority(user.getAuthority().toString()));
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public String getUsername() {
                    return user.getUsername();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return user.isAccountNotLocked();
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };

            if (!user.isAccountNotLocked()) {
                throw new UserAccountLockedException("User account locked: " + username);
            }

            return org.springframework.security.core.userdetails.User.withUserDetails(userDetails).build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
