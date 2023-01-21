package io.github.hossensyedriadh.restdemo.entity;

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
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "Comment")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
public final class Comment extends RepresentationModel<Comment> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7334790241441592372L;

    @Setter(AccessLevel.NONE)
    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private String id;

    @Length(message = "Allowed length is 5-200 characters", min = 5, max = 200)
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    @Column(name = "comment_on", updatable = false, nullable = false)
    @CreatedDate
    private Long commentOn;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(targetEntity = Post.class, cascade = {CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "post_ref", referencedColumnName = "id", updatable = false, nullable = false)
    private Post post;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "comment_by", referencedColumnName = "username", updatable = false, nullable = false)
    @CreatedBy
    private User commentBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    @Column(name = "updated_on", insertable = false)
    @LastModifiedDate
    private Long lastModifiedOn;

    @PrePersist
    private void init() {
        this.id = UUID.randomUUID().toString();
    }
}
