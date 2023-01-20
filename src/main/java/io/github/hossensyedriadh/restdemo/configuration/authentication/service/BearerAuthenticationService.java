package io.github.hossensyedriadh.restdemo.configuration.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.hossensyedriadh.restdemo.entity.RefreshToken;
import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.repository.jpa.RefreshTokenRepository;
import io.github.hossensyedriadh.restdemo.repository.jpa.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Log4j
@Service
public class BearerAuthenticationService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RSAPublicKey rsaPublicKey;
    private final RSAPrivateKey rsaPrivateKey;

    @Autowired
    public BearerAuthenticationService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                                       RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.rsaPublicKey = rsaPublicKey;
        this.rsaPrivateKey = rsaPrivateKey;
    }

    private int accessTokenValidity;

    private final String accessTokenSubject = "Access Token";

    private int refreshTokenValidity;

    private final String refreshTokenSubject = "Refresh Token";

    @Value("${bearer-authentication.tokens.access-token.validity-minutes}")
    public void setAccessTokenValidity(int accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    @Value("${bearer-authentication.tokens.refresh-token.validity-minutes}")
    public void setRefreshTokenValidity(int refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String generateAccessToken(Map<String, String> claims) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(Calendar.MINUTE, this.accessTokenValidity);

        JWTCreator.Builder accessTokenBuilder = JWT.create().withSubject(this.accessTokenSubject);
        claims.forEach(accessTokenBuilder::withClaim);

        return accessTokenBuilder.withNotBefore(new Date()).withIssuedAt(new Date())
                .withExpiresAt(calendar.getTime()).sign(Algorithm.RSA256(this.rsaPublicKey, this.rsaPrivateKey));
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        try {
            Jwt decodedJwt = NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build().decode(token);

            String username = decodedJwt.getClaimAsString("username");
            String subject = decodedJwt.getSubject();

            return username.equals(userDetails.getUsername())
                    && subject.equals(this.accessTokenSubject) && Objects.requireNonNull(decodedJwt.getExpiresAt()).isAfter(Instant.now());
        } catch (Exception e) {
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return false;
        }
    }

    public String getRefreshToken(String username, Map<String, String> claims) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAll()
                .stream().filter(token -> token.getForUser().getUsername().equals(username)).toList();

        if (refreshTokens.size() == 1) {
            RefreshToken token = refreshTokens.get(0);

            try {
                Jwt jwt = NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build().decode(token.getToken());

                if (Objects.requireNonNull(jwt.getExpiresAt()).isAfter(Instant.now()) && jwt.getSubject().equals(this.refreshTokenSubject)) {
                    return token.getToken();
                }

                return this.createRefreshToken(username, claims);
            } catch (JwtValidationException e) {
                log.warn("Invalid refresh token received, generating new token...");
                refreshTokenRepository.delete(token);
                return this.createRefreshToken(username, claims);
            }
        }

        return this.createRefreshToken(username, claims);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            Jwt decodedJwt = NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build().decode(refreshToken);

            List<RefreshToken> tokens = refreshTokenRepository.findAll().stream()
                    .filter(token -> token.getForUser().getUsername().equals(decodedJwt.getClaimAsString("username"))).toList();

            if (tokens.size() == 1) {
                RefreshToken token = tokens.get(0);

                if (token.getToken().equals(decodedJwt.getTokenValue())) {
                    return Objects.requireNonNull(decodedJwt.getExpiresAt()).isAfter(Instant.now())
                            && decodedJwt.getSubject().equals(this.refreshTokenSubject);
                }
            }

            return false;
        } catch (Exception e) {
            log.error(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            return false;
        }
    }

    private User getUser(String username) {
        if (userRepository.findById(username).isPresent()) {
            return userRepository.findById(username).get();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }

    private String createRefreshToken(String username, Map<String, String> claims) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(Calendar.MINUTE, this.refreshTokenValidity);

        JWTCreator.Builder refreshTokenBuilder = JWT.create().withSubject(this.refreshTokenSubject);
        claims.forEach(refreshTokenBuilder::withClaim);

        String id = UUID.randomUUID().toString();

        String token = refreshTokenBuilder.withNotBefore(new Date()).withIssuedAt(new Date())
                .withExpiresAt(calendar.getTime()).withJWTId(id).sign(Algorithm.RSA256(this.rsaPublicKey, this.rsaPrivateKey));

        this.persistRefreshToken(username, id, token);

        return token;
    }

    @Async
    protected void persistRefreshToken(String username, String jwtId, String token) {
        RefreshToken refreshToken = new RefreshToken(jwtId, token, this.getUser(username));
        CompletableFuture<RefreshToken> completableFuture = new CompletableFuture<>();
        Executors.newSingleThreadExecutor().submit(() -> {
            completableFuture.completeAsync(() -> refreshTokenRepository.saveAndFlush(refreshToken));
        });
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build();
    }
}
