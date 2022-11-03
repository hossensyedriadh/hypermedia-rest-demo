package io.github.hossensyedriadh.restdemo.hypermedia.v1;

import io.github.hossensyedriadh.restdemo.controller.v1.resource.CommentController;
import io.github.hossensyedriadh.restdemo.controller.v1.resource.PostController;
import io.github.hossensyedriadh.restdemo.entity.Post;
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
public class PostRepresentationAssembler extends RepresentationModelAssemblerSupport<Post, Post> {
    private int defaultPageSize;

    @Value("${spring.data.rest.default-page-size}")
    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public PostRepresentationAssembler() {
        super(PostController.class, Post.class);
    }

    /**
     * Converts the given entity into a {@code D}, which extends {@link RepresentationModel}.
     *
     * @param entity Post entity class
     * @return Post
     */
    @Override
    @NonNull
    public Post toModel(@NonNull Post entity) {
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                .post(entity.getId())).withSelfRel().withTitle("Get post"));

        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                .update(entity)).withRel(LinkRelation.of("modify")).withTitle("Modify post"));

        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                .delete(entity.getId())).withRel(LinkRelation.of("remove")).withTitle("Delete post"));

        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
                        .comments(entity.getId()))
                .withRel("comments").withTitle("Get comments on post"));

        return entity;
    }

    @Override
    @NonNull
    public CollectionModel<Post> toCollectionModel(@NonNull Iterable<? extends Post> entities) {
        CollectionModel<Post> postCollectionModel = super.toCollectionModel(entities);

        postCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                .create(new Post())).withRel(LinkRelation.of("create")).withTitle("Create post"));

        postCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                .postsList(null)).withRel(IanaLinkRelations.SELF).withTitle("Get list of posts"));

        postCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                .postsList("user")).withRel(IanaLinkRelations.COLLECTION).withTitle("Get list of posts by user"));

        postCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                        .postsPageable(null, 0, this.defaultPageSize, Post.class.getDeclaredFields()[1].getName(),
                                Sort.DEFAULT_DIRECTION.toString().toLowerCase())).withRel(LinkRelation.of("collection"))
                .withTitle("Get pageable list of posts"));

        postCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController.class)
                        .postsPageable("user", 0, this.defaultPageSize, Post.class.getDeclaredFields()[1].getName(),
                                Sort.DEFAULT_DIRECTION.toString().toLowerCase())).withRel(LinkRelation.of("collection"))
                .withTitle("Get pageable list of posts by user"));

        return postCollectionModel;
    }
}
