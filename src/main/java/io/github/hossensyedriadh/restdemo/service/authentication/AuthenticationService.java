package io.github.hossensyedriadh.restdemo.service.authentication;

import io.github.hossensyedriadh.restdemo.configuration.authentication.model.AccessTokenRequest;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.BearerTokenRequest;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.BearerTokenResponse;

public interface AuthenticationService {
    BearerTokenResponse authenticate(BearerTokenRequest bearerTokenRequest);

    BearerTokenResponse renewAccessToken(AccessTokenRequest accessTokenRequest);
}
