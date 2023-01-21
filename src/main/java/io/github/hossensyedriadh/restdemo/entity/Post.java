package io.github.hossensyedriadh.restdemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
public final class Post extends RepresentationModel<Post> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6324028056146547928L;

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private String id;

    @Length(message = "Allowed length is 5-350 characters", min = 5, max = 350)
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    @Column(name = "posted_on", updatable = false, nullable = false)
    @CreatedDate
    private Long postedOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "posted_by", referencedColumnName = "username", updatable = false, nullable = false)
    @CreatedBy
    private User postedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    @Column(name = "updated_on", insertable = false)
    @LastModifiedDate
    private Long lastModifiedOn;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Comment> comments;

    @PrePersist
    private void init() {
        this.id = UUID.randomUUID().toString();
    }
}
