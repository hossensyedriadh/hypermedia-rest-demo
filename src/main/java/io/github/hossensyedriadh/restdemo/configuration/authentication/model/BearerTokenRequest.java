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
public final class BearerTokenRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2494304303913829636L;

    private String username;

    private String passphrase;
}
