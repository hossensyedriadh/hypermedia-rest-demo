package io.github.hossensyedriadh.restdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "RefreshToken")
@Table(name = "refresh_tokens")
public final class RefreshToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 3926246905052059145L;

    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private String id;

    @Column(name = "token", unique = true, updatable = false, nullable = false)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "for_user", referencedColumnName = "username", nullable = false)
    private User forUser;
}
