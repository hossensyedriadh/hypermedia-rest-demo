package io.github.hossensyedriadh.restdemo.controller.v1.resource;

import io.github.hossensyedriadh.restdemo.entity.Comment;
import io.github.hossensyedriadh.restdemo.hypermedia.v1.CommentRepresentationAssembler;
import io.github.hossensyedriadh.restdemo.service.comment.CommentService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/comments", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
public class CommentController {
    private final CommentService commentService;

    private final CommentRepresentationAssembler commentRepresentationAssembler;

    @Autowired
    public CommentController(CommentService commentService,
                             CommentRepresentationAssembler commentRepresentationAssembler) {
        this.commentService = commentService;
        this.commentRepresentationAssembler = commentRepresentationAssembler;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "_embedded": {
                                    "comments": [
                                        {
                                            "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                            "content": "some comment by riadh",
                                            "commentOn": "2022-10-31T18:07:16",
                                            "commentBy": {
                                                "username": "syedriadhhossen",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_ADMINISTRATOR"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Get comment"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/comments/",
                                                    "title": "Modify comment"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Delete comment"
                                                }
                                            }
                                        }
                                    ]
                                },
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/%2F%7BpostId%7D/list",
                                        "title": "Get list comments on post"
                                    },
                                    "create": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Create comment"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "_embedded": {
                                    "comments": [
                                        {
                                            "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                            "content": "some comment by riadh",
                                            "commentOn": "2022-10-31T18:07:16",
                                            "commentBy": {
                                                "username": "syedriadhhossen",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_ADMINISTRATOR"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Get comment"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/comments/",
                                                    "title": "Modify comment"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Delete comment"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "commentBy",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "commentOn",
                                                            "readOnly": true,
                                                            "type": "datetime-local"
                                                        },
                                                        {
                                                            "name": "content",
                                                            "required": true,
                                                            "minLength": 5,
                                                            "maxLength": 200,
                                                            "type": "text"
                                                        },
                                                        {
                                                            "name": "id",
                                                            "readOnly": true,
                                                            "type": "text"
                                                        },
                                                        {
                                                            "name": "lastModifiedOn",
                                                            "readOnly": true,
                                                            "type": "datetime-local"
                                                        },
                                                        {
                                                            "name": "post"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/comments/"
                                                },
                                                "delete": {
                                                    "method": "DELETE",
                                                    "properties": []
                                                }
                                            }
                                        }
                                    ]
                                },
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/%2F%7BpostId%7D/list",
                                        "title": "Get list comments on post"
                                    },
                                    "create": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Create comment"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "POST",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "commentBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "commentOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 200,
                                                "type": "text"
                                            },
                                            {
                                                "name": "id",
                                                "readOnly": true,
                                                "type": "text"
                                            },
                                            {
                                                "name": "lastModifiedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "post"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/comments/"
                                    }
                                }
                            }
                            """)
            })),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 06:43:34 PM",
                                "message": "Authentication is required to access this resource",
                                "error": "Unauthorized",
                                "path": "/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get list of comments on a post",
            description = "Returns a list of comments on a post using given post id in path variable")
    @GetMapping("/{postId}/list")
    public ResponseEntity<?> comments(@PathVariable("postId") String postId) {
        List<Comment> comments = this.commentService.comments(postId);

        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        CollectionModel<Comment> commentCollectionModel = this.commentRepresentationAssembler.toCollectionModel(comments);

        return new ResponseEntity<>(commentCollectionModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "content": "some comment by riadh",
                                "commentOn": "2022-10-31T18:07:16",
                                "commentBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get comment"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Modify comment"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete comment"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "content": "some comment by riadh",
                                "commentOn": "2022-10-31T18:07:16",
                                "commentBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get comment"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Modify comment"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete comment"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "commentBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "commentOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 200,
                                                "type": "text"
                                            },
                                            {
                                                "name": "id",
                                                "readOnly": true,
                                                "type": "text"
                                            },
                                            {
                                                "name": "lastModifiedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "post"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/comments/"
                                    },
                                    "delete": {
                                        "method": "DELETE",
                                        "properties": []
                                    }
                                }
                            }
                            """)
            })),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 06:43:34 PM",
                                "message": "Authentication is required to access this resource",
                                "error": "Unauthorized",
                                "path": "/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get a comment by id",
            description = "Returns a comment by the given id of the post in the path variable")
    @GetMapping("/{id}")
    public ResponseEntity<?> comment(@PathVariable("id") String id) {
        Optional<Comment> comment = this.commentService.comment(id);

        if (comment.isPresent()) {
            Comment commentModel = this.commentRepresentationAssembler.toModel(comment.get());
            return new ResponseEntity<>(commentModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "content": "some comment by riadh",
                                "commentOn": "2022-10-31T18:07:16",
                                "commentBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get comment"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Modify comment"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete comment"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "content": "some comment by riadh",
                                "commentOn": "2022-10-31T18:07:16",
                                "commentBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get comment"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Modify comment"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete comment"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "commentBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "commentOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 200,
                                                "type": "text"
                                            },
                                            {
                                                "name": "id",
                                                "readOnly": true,
                                                "type": "text"
                                            },
                                            {
                                                "name": "lastModifiedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "post"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/comments/"
                                    },
                                    "delete": {
                                        "method": "DELETE",
                                        "properties": []
                                    }
                                }
                            }
                            """)
            })),
            @ApiResponse(code = 401, message = "Unauthorized", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 06:43:34 PM",
                                "message": "Authentication is required to access this resource",
                                "error": "Unauthorized",
                                "path": "/api/v1/comments/"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/comments/"
                            }
                            """)
            }))
    })
    @Operation(method = "POST", summary = "Create a comment on a post",
            description = "Creates and returns a comment on a post using the validated request body")
    @PostMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@Valid @RequestBody Comment comment) {
        Comment createdComment = this.commentService.create(comment);
        Comment commentModel = this.commentRepresentationAssembler.toModel(createdComment);

        return new ResponseEntity<>(commentModel, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "content": "some comment by riadh",
                                "commentOn": "2022-10-31T18:07:16",
                                "commentBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get comment"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Modify comment"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete comment"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "id": "8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "content": "some comment by riadh",
                                "commentOn": "2022-10-31T18:07:16",
                                "commentBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get comment"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/comments/",
                                        "title": "Modify comment"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete comment"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "commentBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "commentOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 200,
                                                "type": "text"
                                            },
                                            {
                                                "name": "id",
                                                "readOnly": true,
                                                "type": "text"
                                            },
                                            {
                                                "name": "lastModifiedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            },
                                            {
                                                "name": "post"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/comments/"
                                    },
                                    "delete": {
                                        "method": "DELETE",
                                        "properties": []
                                    }
                                }
                            }
                            """)
            })),
            @ApiResponse(code = 400, message = "Bad Request", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 400,
                                "timestamp": "2022-11-01 01:50:52 AM",
                                "message": "Comment not found with ID: 8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "error": "Bad Request",
                                "path": "/api/v1/comments/"
                            }
                            """)
            })),
            @ApiResponse(code = 401, message = "Unauthorized", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 06:43:34 PM",
                                "message": "Authentication is required to access this resource",
                                "error": "Unauthorized",
                                "path": "/api/v1/comments/"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/comments/"
                            }
                            """)
            }))
    })
    @Operation(method = "PUT", summary = "Modify a comment",
            description = "Modifies and returns a comment using the validated request body. This action can be performed by the commenter only")
    @PutMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@Valid @RequestBody Comment comment) {
        Comment modifiedComment = this.commentService.update(comment);
        Comment commentModel = this.commentRepresentationAssembler.toModel(modifiedComment);

        return new ResponseEntity<>(commentModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 400,
                                "timestamp": "2022-11-01 01:50:52 AM",
                                "message": "Comment not found with ID: 8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "error": "Bad Request",
                                "path": "/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """)
            })),
            @ApiResponse(code = 401, message = "Unauthorized", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 06:43:34 PM",
                                "message": "Authentication is required to access this resource",
                                "error": "Unauthorized",
                                "path": "/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/comments/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """)
            }))
    })
    @Operation(method = "DELETE", summary = "Delete a comment",
            description = "Deletes a comment by the given id of the comment in the path variable. This action can be performed by the original poster, commenter and administrator only")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        this.commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
