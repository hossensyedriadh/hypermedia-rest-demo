package io.github.hossensyedriadh.restdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hossensyedriadh.restdemo.configuration.datasource.PostgreSQLEnumType;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "User")
@Table(name = "users")
@TypeDef(name = "user_authorities", typeClass = PostgreSQLEnumType.class)
public final class User extends RepresentationModel<User> implements Serializable {
    @Serial
    private static final long serialVersionUID = -2696101007060694258L;

    @NotNull
    @Id
    @Column(name = "username", unique = true, updatable = false, nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password", updatable = false, nullable = false)
    private String password;

    @NotNull
    @Column(name = "is_account_not_locked", nullable = false)
    private boolean accountNotLocked;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    @Type(type = "user_authorities")
    @Column(name = "authority", updatable = false, nullable = false)
    private Authority authority;
}
