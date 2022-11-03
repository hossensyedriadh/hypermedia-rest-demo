package io.github.hossensyedriadh.restdemo.hypermedia.v1;

import io.github.hossensyedriadh.restdemo.controller.v1.resource.UserController;
import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
import io.github.hossensyedriadh.restdemo.service.CurrentAuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserRepresentationAssembler extends RepresentationModelAssemblerSupport<User, User> {
    private final CurrentAuthenticationContext currentAuthenticationContext;

    @Autowired
    public UserRepresentationAssembler(CurrentAuthenticationContext currentAuthenticationContext) {
        super(UserController.class, User.class);
        this.currentAuthenticationContext = currentAuthenticationContext;
    }

    private int defaultPageSize;

    @Value("${spring.data.rest.default-page-size}")
    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    /**
     * Converts the given entity into a {@code D}, which extends {@link RepresentationModel}.
     *
     * @param entity User entity class
     * @return User
     */
    @Override
    @NonNull
    public User toModel(@NonNull User entity) {
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .user(entity.getUsername())).withSelfRel().withTitle("Get user"));

        if (this.currentAuthenticationContext.getPrincipalRole().equals(Authority.ROLE_ADMINISTRATOR.toString())) {
            entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                    .update(entity)).withRel(LinkRelation.of("modify")).withTitle("Modify user"));
        }

        return entity;
    }

    @Override
    @NonNull
    public CollectionModel<User> toCollectionModel(@NonNull Iterable<? extends User> entities) {
        CollectionModel<User> userCollectionModel = super.toCollectionModel(entities);
        
        userCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .usersList()).withRel(IanaLinkRelations.SELF).withTitle("Get list of users"));

        userCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .usersPageable(0, this.defaultPageSize, User.class.getDeclaredFields()[1].getName(),
                                Sort.DEFAULT_DIRECTION.toString().toLowerCase())).withRel("collection")
                .withTitle("Get pageable list of users"));

        return userCollectionModel;
    }
}
