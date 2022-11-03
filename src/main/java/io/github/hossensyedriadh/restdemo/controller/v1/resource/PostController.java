package io.github.hossensyedriadh.restdemo.controller.v1.resource;

import io.github.hossensyedriadh.restdemo.entity.Post;
import io.github.hossensyedriadh.restdemo.hypermedia.v1.PostRepresentationAssembler;
import io.github.hossensyedriadh.restdemo.service.post.PostService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/posts", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
public class PostController {
    private final PostService postService;
    private final PagedResourcesAssembler<Post> postPagedResourcesAssembler;
    private final PostRepresentationAssembler postRepresentationAssembler;

    @Autowired
    public PostController(PostService postService, PagedResourcesAssembler<Post> postPagedResourcesAssembler,
                          PostRepresentationAssembler postRepresentationAssembler) {
        this.postService = postService;
        this.postPagedResourcesAssembler = postPagedResourcesAssembler;
        this.postRepresentationAssembler = postRepresentationAssembler;
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
                                "posts": [
                                  {
                                    "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                    "content": "Some post by riadh...",
                                    "postedOn": "2022-10-31T18:03:24",
                                    "postedBy": {
                                      "username": "syedriadhhossen",
                                      "accountNotLocked": true,
                                      "authority": "ROLE_ADMINISTRATOR"
                                    },
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                      },
                                      "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                      },
                                      "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                      },
                                      "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                      }
                                    }
                                  },
                                  {
                                    "id": "0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                    "content": "Some post by john...",
                                    "postedOn": "2022-10-31T18:03:24",
                                    "postedBy": {
                                      "username": "john",
                                      "accountNotLocked": true,
                                      "authority": "ROLE_USER"
                                    },
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                      },
                                      "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                      },
                                      "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                      },
                                      "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552caa5-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                      }
                                    }
                                  },
                                  {
                                    "id": "903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                    "content": "Again some post by riadh...",
                                    "postedOn": "2022-10-31T20:57:42",
                                    "postedBy": {
                                      "username": "syedriadhhossen",
                                      "accountNotLocked": true,
                                      "authority": "ROLE_ADMINISTRATOR"
                                    },
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                        "title": "Get post"
                                      },
                                      "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                      },
                                      "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                        "title": "Delete post"
                                      },
                                      "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/903de6b1-8599-4b96-b405-9cb7aa8960cb/list",
                                        "title": "Get comments on post"
                                      }
                                    }
                                  }
                                ]
                              },
                              "_links": {
                                "self": {
                                  "href": "http://localhost:8080/api/v1/posts/?page=0&size=10&sort=id,asc"
                                }
                              },
                              "page": {
                                "size": 10,
                                "totalElements": 3,
                                "totalPages": 1,
                                "number": 0
                              }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                              "_embedded": {
                                "posts": [
                                  {
                                    "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                    "content": "Some post by riadh...",
                                    "postedOn": "2022-10-31T18:03:24",
                                    "postedBy": {
                                      "username": "syedriadhhossen",
                                      "accountNotLocked": true,
                                      "authority": "ROLE_ADMINISTRATOR"
                                    },
                                    "lastModifiedOn": null,
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                      },
                                      "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                      },
                                      "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                      },
                                      "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                      }
                                    },
                                    "_templates": {
                                      "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                          {
                                            "name": "content",
                                            "required": true,
                                            "minLength": 5,
                                            "maxLength": 350,
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
                                            "name": "postedBy",
                                            "readOnly": true
                                          },
                                          {
                                            "name": "postedOn",
                                            "readOnly": true,
                                            "type": "datetime-local"
                                          }
                                        ],
                                        "target": "http://localhost:8080/api/v1/posts/"
                                      },
                                      "delete": {
                                        "method": "DELETE",
                                        "properties": []
                                      }
                                    }
                                  },
                                  {
                                    "id": "0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                    "content": "Some post by john...",
                                    "postedOn": "2022-10-31T18:03:24",
                                    "postedBy": {
                                      "username": "john",
                                      "accountNotLocked": true,
                                      "authority": "ROLE_USER"
                                    },
                                    "lastModifiedOn": null,
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                      },
                                      "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                      },
                                      "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                      },
                                      "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552caa5-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                      }
                                    },
                                    "_templates": {
                                      "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                          {
                                            "name": "content",
                                            "required": true,
                                            "minLength": 5,
                                            "maxLength": 350,
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
                                            "name": "postedBy",
                                            "readOnly": true
                                          },
                                          {
                                            "name": "postedOn",
                                            "readOnly": true,
                                            "type": "datetime-local"
                                          }
                                        ],
                                        "target": "http://localhost:8080/api/v1/posts/"
                                      },
                                      "delete": {
                                        "method": "DELETE",
                                        "properties": []
                                      }
                                    }
                                  },
                                  {
                                    "id": "903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                    "content": "Again some post by riadh...",
                                    "postedOn": "2022-10-31T20:57:42",
                                    "postedBy": {
                                      "username": "syedriadhhossen",
                                      "accountNotLocked": true,
                                      "authority": "ROLE_ADMINISTRATOR"
                                    },
                                    "lastModifiedOn": null,
                                    "_links": {
                                      "self": {
                                        "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                        "title": "Get post"
                                      },
                                      "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                      },
                                      "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                        "title": "Delete post"
                                      },
                                      "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/903de6b1-8599-4b96-b405-9cb7aa8960cb/list",
                                        "title": "Get comments on post"
                                      }
                                    },
                                    "_templates": {
                                      "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                          {
                                            "name": "content",
                                            "required": true,
                                            "minLength": 5,
                                            "maxLength": 350,
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
                                            "name": "postedBy",
                                            "readOnly": true
                                          },
                                          {
                                            "name": "postedOn",
                                            "readOnly": true,
                                            "type": "datetime-local"
                                          }
                                        ],
                                        "target": "http://localhost:8080/api/v1/posts/"
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
                                  "href": "http://localhost:8080/api/v1/posts/?page=0&size=10&sort=id,asc"
                                }
                              },
                              "page": {
                                "size": 10,
                                "totalElements": 3,
                                "totalPages": 1,
                                "number": 0
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
                                "path": "/api/v1/posts/"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get pageable collection of posts",
            description = "Returns a pageable collection of posts with embedded Hypermedia links")
    @GetMapping("/")
    public ResponseEntity<?> postsPageable(@RequestParam(value = "postsBy", required = false) String postsBy, @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "sort", defaultValue = "id,asc") String... sort) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(new Sort.Order(Sort.Direction.fromOptionalString(sort[1]).orElse(Sort.Direction.ASC), sort[0]));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));

        Page<Post> postPage;

        if (postsBy != null) {
            postPage = this.postService.posts(pageable, postsBy);
        } else {
            postPage = this.postService.posts(pageable);
        }

        if (postPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PagedModel<Post> postPagedModel = this.postPagedResourcesAssembler.toModel(postPage, this.postRepresentationAssembler);

        return new ResponseEntity<>(postPagedModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "_embedded": {
                                    "posts": [
                                        {
                                            "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                            "content": "Some post by riadh...",
                                            "postedOn": "2022-10-31T18:03:24",
                                            "postedBy": {
                                                "username": "syedriadhhossen",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_ADMINISTRATOR"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Get post"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/posts/",
                                                    "title": "Modify post"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Delete post"
                                                },
                                                "comments": {
                                                    "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                                    "title": "Get comments on post"
                                                }
                                            }
                                        },
                                        {
                                            "id": "0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                            "content": "Some post by john...",
                                            "postedOn": "2022-10-31T18:03:24",
                                            "postedBy": {
                                                "username": "john",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_USER"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Get post"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/posts/",
                                                    "title": "Modify post"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Delete post"
                                                },
                                                "comments": {
                                                    "href": "http://localhost:8080/api/v1/comments/0552caa5-5914-11ed-9833-1cb72c0f8a92/list",
                                                    "title": "Get comments on post"
                                                }
                                            }
                                        },
                                        {
                                            "id": "903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                            "content": "Again some post by riadh...",
                                            "postedOn": "2022-10-31T20:57:42",
                                            "postedBy": {
                                                "username": "syedriadhhossen",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_ADMINISTRATOR"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                                    "title": "Get post"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/posts/",
                                                    "title": "Modify post"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                                    "title": "Delete post"
                                                },
                                                "comments": {
                                                    "href": "http://localhost:8080/api/v1/comments/903de6b1-8599-4b96-b405-9cb7aa8960cb/list",
                                                    "title": "Get comments on post"
                                                }
                                            }
                                        }
                                    ]
                                },
                                "_links": {
                                    "create": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Create post"
                                    },
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/list{?postsBy}",
                                        "title": "Get list of posts",
                                        "templated": true
                                    },
                                    "collection": [
                                        {
                                            "href": "http://localhost:8080/api/v1/posts/list?postsBy=user",
                                            "title": "Get list of posts by user"
                                        },
                                        {
                                            "href": "http://localhost:8080/api/v1/posts/?page=0&size=10&sort=id%2Casc{&postsBy}",
                                            "title": "Get pageable list of posts",
                                            "templated": true
                                        },
                                        {
                                            "href": "http://localhost:8080/api/v1/posts/?postsBy=user&page=0&size=10&sort=id%2Casc",
                                            "title": "Get pageable list of posts by user"
                                        }
                                    ]
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "_embedded": {
                                    "posts": [
                                        {
                                            "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                            "content": "Some post by riadh...",
                                            "postedOn": "2022-10-31T18:03:24",
                                            "postedBy": {
                                                "username": "syedriadhhossen",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_ADMINISTRATOR"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Get post"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/posts/",
                                                    "title": "Modify post"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Delete post"
                                                },
                                                "comments": {
                                                    "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                                    "title": "Get comments on post"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "content",
                                                            "required": true,
                                                            "minLength": 5,
                                                            "maxLength": 350,
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
                                                            "name": "postedBy",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "postedOn",
                                                            "readOnly": true,
                                                            "type": "datetime-local"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/posts/"
                                                },
                                                "delete": {
                                                    "method": "DELETE",
                                                    "properties": []
                                                }
                                            }
                                        },
                                        {
                                            "id": "0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                            "content": "Some post by john...",
                                            "postedOn": "2022-10-31T18:03:24",
                                            "postedBy": {
                                                "username": "john",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_USER"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Get post"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/posts/",
                                                    "title": "Modify post"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/posts/0552caa5-5914-11ed-9833-1cb72c0f8a92",
                                                    "title": "Delete post"
                                                },
                                                "comments": {
                                                    "href": "http://localhost:8080/api/v1/comments/0552caa5-5914-11ed-9833-1cb72c0f8a92/list",
                                                    "title": "Get comments on post"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "content",
                                                            "required": true,
                                                            "minLength": 5,
                                                            "maxLength": 350,
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
                                                            "name": "postedBy",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "postedOn",
                                                            "readOnly": true,
                                                            "type": "datetime-local"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/posts/"
                                                },
                                                "delete": {
                                                    "method": "DELETE",
                                                    "properties": []
                                                }
                                            }
                                        },
                                        {
                                            "id": "903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                            "content": "Again some post by riadh...",
                                            "postedOn": "2022-10-31T20:57:42",
                                            "postedBy": {
                                                "username": "syedriadhhossen",
                                                "accountNotLocked": true,
                                                "authority": "ROLE_ADMINISTRATOR"
                                            },
                                            "lastModifiedOn": null,
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                                    "title": "Get post"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/posts/",
                                                    "title": "Modify post"
                                                },
                                                "remove": {
                                                    "href": "http://localhost:8080/api/v1/posts/903de6b1-8599-4b96-b405-9cb7aa8960cb",
                                                    "title": "Delete post"
                                                },
                                                "comments": {
                                                    "href": "http://localhost:8080/api/v1/comments/903de6b1-8599-4b96-b405-9cb7aa8960cb/list",
                                                    "title": "Get comments on post"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "content",
                                                            "required": true,
                                                            "minLength": 5,
                                                            "maxLength": 350,
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
                                                            "name": "postedBy",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "postedOn",
                                                            "readOnly": true,
                                                            "type": "datetime-local"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/posts/"
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
                                    "create": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Create post"
                                    },
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/list{?postsBy}",
                                        "title": "Get list of posts",
                                        "templated": true
                                    },
                                    "collection": [
                                        {
                                            "href": "http://localhost:8080/api/v1/posts/list?postsBy=user",
                                            "title": "Get list of posts by user"
                                        },
                                        {
                                            "href": "http://localhost:8080/api/v1/posts/?page=0&size=10&sort=id%2Casc{&postsBy}",
                                            "title": "Get pageable list of posts",
                                            "templated": true
                                        },
                                        {
                                            "href": "http://localhost:8080/api/v1/posts/?postsBy=user&page=0&size=10&sort=id%2Casc",
                                            "title": "Get pageable list of posts by user"
                                        }
                                    ]
                                },
                                "_templates": {
                                    "default": {
                                        "method": "POST",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 350,
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
                                                "name": "postedBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "postedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/posts/"
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
                                "path": "/api/v1/posts/list"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/posts/list"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get list of posts",
            description = "Returns a list of posts with embedded Hypermedia links")
    @GetMapping("/list")
    public ResponseEntity<?> postsList(@RequestParam(value = "postsBy", required = false) String postsBy) {
        List<Post> posts;

        if (postsBy != null) {
            posts = this.postService.posts(postsBy);
        } else {
            posts = this.postService.posts();
        }

        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        CollectionModel<Post> postCollectionModel = this.postRepresentationAssembler.toCollectionModel(posts);

        return new ResponseEntity<>(postCollectionModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                "content": "Some post by riadh...",
                                "postedOn": "2022-10-31T18:03:24",
                                "postedBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                    },
                                    "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                "content": "Some post by riadh...",
                                "postedOn": "2022-10-31T18:03:24",
                                "postedBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                    },
                                    "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 350,
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
                                                "name": "postedBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "postedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/posts/"
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
                                "path": "/api/v1/posts/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/posts/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get a post by id",
            description = "Returns a post by the given id of the post in the path variable")
    @GetMapping("/{id}")
    public ResponseEntity<?> post(@PathVariable("id") String id) {
        Optional<Post> post = this.postService.post(id);
        if (post.isPresent()) {
            Post postModel = this.postRepresentationAssembler.toModel(post.get());
            return new ResponseEntity<>(postModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                "content": "Some post by riadh...",
                                "postedOn": "2022-10-31T18:03:24",
                                "postedBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                    },
                                    "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                "content": "Some post by riadh...",
                                "postedOn": "2022-10-31T18:03:24",
                                "postedBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                    },
                                    "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 350,
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
                                                "name": "postedBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "postedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/posts/"
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
                                "path": "/api/v1/posts/"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/posts/"
                            }
                            """)
            }))
    })
    @Operation(method = "POST", summary = "Create a post",
            description = "Creates and returns a post using the validated request body")
    @PostMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@Valid @RequestBody Post post) {
        Post createdPost = this.postService.create(post);
        Post postModel = this.postRepresentationAssembler.toModel(createdPost);

        return new ResponseEntity<>(postModel, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                "content": "Some post by riadh...",
                                "postedOn": "2022-10-31T18:03:24",
                                "postedBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                    },
                                    "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "id": "0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                "content": "Some post by riadh...",
                                "postedOn": "2022-10-31T18:03:24",
                                "postedBy": {
                                    "username": "syedriadhhossen",
                                    "accountNotLocked": true,
                                    "authority": "ROLE_ADMINISTRATOR"
                                },
                                "lastModifiedOn": null,
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Get post"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/posts/",
                                        "title": "Modify post"
                                    },
                                    "remove": {
                                        "href": "http://localhost:8080/api/v1/posts/0552b6a8-5914-11ed-9833-1cb72c0f8a92",
                                        "title": "Delete post"
                                    },
                                    "comments": {
                                        "href": "http://localhost:8080/api/v1/comments/0552b6a8-5914-11ed-9833-1cb72c0f8a92/list",
                                        "title": "Get comments on post"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "content",
                                                "required": true,
                                                "minLength": 5,
                                                "maxLength": 350,
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
                                                "name": "postedBy",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "postedOn",
                                                "readOnly": true,
                                                "type": "datetime-local"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/posts/"
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
                                "message": "Post not found with ID: 8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "error": "Bad Request",
                                "path": "/api/v1/posts/"
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
                                "path": "/api/v1/posts/"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/posts/"
                            }
                            """)
            }))
    })
    @Operation(method = "PUT", summary = "Modify a post",
            description = "Modifies and returns a post using the validated request body. This action can be performed by the original poster only")
    @PutMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@Valid @RequestBody Post post) {
        Post modifiedPost = this.postService.update(post);
        Post postModel = this.postRepresentationAssembler.toModel(modifiedPost);

        return new ResponseEntity<>(postModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 400,
                                "timestamp": "2022-11-01 01:50:52 AM",
                                "message": "Post not found with ID: 8fdd545e-5914-11ed-9833-1cb72c0f8a92",
                                "error": "Bad Request",
                                "path": "/api/v1/posts/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
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
                                "path": "/api/v1/posts/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/posts/8fdd545e-5914-11ed-9833-1cb72c0f8a92"
                            }
                            """)
            }))
    })
    @Operation(method = "DELETE", summary = "Delete a post",
            description = "Deletes a post by the given id of the post in the path variable. This action can be performed by the original poster and administrator only")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        this.postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
