package io.github.hossensyedriadh.restdemo.hypermedia.v1;

import io.github.hossensyedriadh.restdemo.controller.v1.resource.CommentController;
import io.github.hossensyedriadh.restdemo.entity.Comment;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CommentRepresentationAssembler extends RepresentationModelAssemblerSupport<Comment, Comment> {
    public CommentRepresentationAssembler() {
        super(CommentController.class, Comment.class);
    }

    /**
     * Converts the given entity into a {@code D}, which extends {@link RepresentationModel}.
     *
     * @param entity Comment entity class
     * @return Comment
     */
    @Override
    @NonNull
    public Comment toModel(@NonNull Comment entity) {
        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
                .comment(entity.getId())).withSelfRel().withTitle("Get comment"));

        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
                .update(entity)).withRel(LinkRelation.of("modify")).withTitle("Modify comment"));

        entity.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
                .delete(entity.getId())).withRel(LinkRelation.of("remove")).withTitle("Delete comment"));

        return entity;
    }

    @Override
    @NonNull
    public CollectionModel<Comment> toCollectionModel(@NonNull Iterable<? extends Comment> entities) {
        CollectionModel<Comment> commentCollectionModel = super.toCollectionModel(entities);
        commentCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
                .comments("/{postId}")).withRel(IanaLinkRelations.SELF).withTitle("Get list comments on post"));

        commentCollectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CommentController.class)
                .create(new Comment())).withRel(LinkRelation.of("create")).withTitle("Create comment"));

        return commentCollectionModel;
    }
}
