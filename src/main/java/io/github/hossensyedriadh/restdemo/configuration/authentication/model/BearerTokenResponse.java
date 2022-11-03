package io.github.hossensyedriadh.restdemo.configuration.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class BearerTokenResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4664821098266464674L;

    private String access_token;

    private String access_token_type;

    private String refresh_token;
}
