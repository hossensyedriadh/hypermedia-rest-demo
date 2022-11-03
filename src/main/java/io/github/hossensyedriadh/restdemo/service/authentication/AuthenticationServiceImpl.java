package io.github.hossensyedriadh.restdemo.service.authentication;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.AccessTokenRequest;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.BearerTokenRequest;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.BearerTokenResponse;
import io.github.hossensyedriadh.restdemo.configuration.authentication.service.BearerAuthenticationService;
import io.github.hossensyedriadh.restdemo.configuration.authentication.service.BearerAuthenticationUserDetailsService;
import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.exception.InvalidCredentialsException;
import io.github.hossensyedriadh.restdemo.exception.InvalidRefreshTokenException;
import io.github.hossensyedriadh.restdemo.exception.UserAccountLockedException;
import io.github.hossensyedriadh.restdemo.repository.jpa.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final BearerAuthenticationService bearerAuthenticationService;
    private final BearerAuthenticationUserDetailsService bearerAuthenticationUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private LoadingCache<String, String> accessTokenCache;

    @Autowired
    public AuthenticationServiceImpl(BearerAuthenticationService bearerAuthenticationService,
                                     BearerAuthenticationUserDetailsService bearerAuthenticationUserDetailsService,
                                     PasswordEncoder passwordEncoder, UserRepository userRepository,
                                     HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.bearerAuthenticationService = bearerAuthenticationService;
        this.bearerAuthenticationUserDetailsService = bearerAuthenticationUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Value("${bearer-authentication.tokens.access-token.type}")
    private String accessTokenType;

    private int accessTokenValidity;

    @Value("${bearer-authentication.tokens.access-token.validity-minutes}")
    public void setAccessTokenValidity(int accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    @PostConstruct
    private void initializeCache() {
        this.accessTokenCache = CacheBuilder.newBuilder().expireAfterWrite(this.accessTokenValidity, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public String load(@NonNull String key) {
                        return "";
                    }
                });
    }

    @Override
    public BearerTokenResponse authenticate(BearerTokenRequest bearerTokenRequest) {
        UserDetails userDetails;

        try {
            userDetails = this.bearerAuthenticationUserDetailsService.loadUserByUsername(bearerTokenRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (UserAccountLockedException e) {
            throw new UserAccountLockedException(e.getMessage());
        }

        if (this.passwordEncoder.matches(bearerTokenRequest.getPassphrase(), userDetails.getPassword())) {
            try {
                if (this.accessTokenCache.asMap().containsKey(userDetails.getUsername())) {
                    this.accessTokenCache.refresh(userDetails.getUsername());
                    String existingToken = this.accessTokenCache.get(userDetails.getUsername());

                    if (this.bearerAuthenticationService.isAccessTokenValid(existingToken, userDetails)) {
                        Map<String, String> claims = new HashMap<>();
                        claims.put("username", userDetails.getUsername());
                        claims.put("authority", String.valueOf(userDetails.getAuthorities().toArray()[0]));

                        String refreshToken = this.bearerAuthenticationService.getRefreshToken(userDetails.getUsername(), claims);

                        this.httpServletResponse.addHeader(HttpHeaders.EXPIRES, String.valueOf(LocalDateTime.ofInstant(
                                Objects.requireNonNull(this.bearerAuthenticationService.jwtDecoder().decode(existingToken).getExpiresAt()),
                                ZoneId.systemDefault())));

                        return new BearerTokenResponse(existingToken, this.accessTokenType, refreshToken);
                    }
                } else {
                    if (this.userRepository.findById(userDetails.getUsername()).isPresent()) {
                        User user = this.userRepository.findById(userDetails.getUsername()).get();

                        Map<String, String> claims = new HashMap<>();
                        claims.put("username", userDetails.getUsername());
                        claims.put("authority", user.getAuthority().toString());

                        String accessToken = this.bearerAuthenticationService.generateAccessToken(claims);
                        String refreshToken = this.bearerAuthenticationService.getRefreshToken(user.getUsername(), claims);

                        this.accessTokenCache.invalidate(userDetails.getUsername());
                        this.accessTokenCache.put(userDetails.getUsername(), accessToken);
                        this.accessTokenCache.refresh(userDetails.getUsername());

                        this.httpServletResponse.addHeader(HttpHeaders.EXPIRES, String.valueOf(LocalDateTime.ofInstant(
                                Objects.requireNonNull(this.bearerAuthenticationService.jwtDecoder().decode(accessToken).getExpiresAt()),
                                ZoneId.systemDefault())));

                        return new BearerTokenResponse(accessToken, this.accessTokenType, refreshToken);
                    } else {
                        throw new UsernameNotFoundException("User not found: " + userDetails.getUsername());
                    }
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        this.httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        throw new InvalidCredentialsException("Invalid credentials", httpServletRequest);
    }

    @Override
    public BearerTokenResponse renewAccessToken(AccessTokenRequest accessTokenRequest) {
        String refreshToken = accessTokenRequest.getRefresh_token();
        String username;

        try {
            username = this.bearerAuthenticationService.jwtDecoder().decode(refreshToken).getClaimAsString("username");
        } catch (JwtException e) {
            throw new InvalidRefreshTokenException("Invalid refresh token", httpServletRequest);
        }

        boolean isRefreshTokenValid = this.bearerAuthenticationService.isRefreshTokenValid(refreshToken);

        if (isRefreshTokenValid) {
            if (this.accessTokenCache.asMap().containsKey(username)) {
                try {
                    this.accessTokenCache.refresh(username);
                    String accessToken = this.accessTokenCache.get(username);
                    boolean isAccessTokenValid = this.bearerAuthenticationService.isAccessTokenValid(accessToken,
                            this.bearerAuthenticationUserDetailsService.loadUserByUsername(username));

                    if (isAccessTokenValid) {
                        return new BearerTokenResponse(accessToken, this.accessTokenType, refreshToken);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    log.error(e);
                }
            }

            Jwt jwt = this.bearerAuthenticationService.jwtDecoder().decode(refreshToken);

            Map<String, Object> claims = jwt.getClaims();
            Map<String, String> convertedClaims = new HashMap<>();

            convertedClaims.put("username", claims.get("username").toString());
            convertedClaims.put("authority", claims.get("authority").toString());

            String accessToken = this.bearerAuthenticationService.generateAccessToken(convertedClaims);

            this.accessTokenCache.invalidate(username);
            this.accessTokenCache.put(username, accessToken);
            this.accessTokenCache.refresh(username);

            httpServletResponse.addHeader(HttpHeaders.EXPIRES, String.valueOf(LocalDateTime.ofInstant(
                    Objects.requireNonNull(this.bearerAuthenticationService.jwtDecoder().decode(accessToken).getExpiresAt()),
                    ZoneId.systemDefault())));

            return new BearerTokenResponse(accessToken, this.accessTokenType, refreshToken);

        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new InvalidRefreshTokenException("Invalid refresh token", httpServletRequest);
        }
    }
}
