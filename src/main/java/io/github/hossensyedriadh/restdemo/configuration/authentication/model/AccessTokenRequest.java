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
public final class AccessTokenRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -156548545441236603L;

    private String refresh_token;
}
