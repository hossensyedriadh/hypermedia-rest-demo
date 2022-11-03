package io.github.hossensyedriadh.restdemo.controller.v1.resource;

import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.hypermedia.v1.UserRepresentationAssembler;
import io.github.hossensyedriadh.restdemo.service.user.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/users", produces = {MediaTypes.HAL_JSON_VALUE, MediaTypes.HAL_FORMS_JSON_VALUE})
public class UserController {
    private final UserService userService;
    private final UserRepresentationAssembler userRepresentationAssembler;
    private final PagedResourcesAssembler<User> userPagedResourcesAssembler;

    @Autowired
    public UserController(UserService userService, UserRepresentationAssembler userRepresentationAssembler,
                          PagedResourcesAssembler<User> userPagedResourcesAssembler) {
        this.userService = userService;
        this.userRepresentationAssembler = userRepresentationAssembler;
        this.userPagedResourcesAssembler = userPagedResourcesAssembler;
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
                                    "users": [
                                        {
                                            "username": "john",
                                            "accountNotLocked": true,
                                            "authority": "ROLE_USER",
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/users/john",
                                                    "title": "Get user"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/users/",
                                                    "title": "Modify user"
                                                }
                                            }
                                        },
                                        {
                                            "username": "syedriadhhossen",
                                            "accountNotLocked": true,
                                            "authority": "ROLE_ADMINISTRATOR",
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                                    "title": "Get user"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/users/",
                                                    "title": "Modify user"
                                                }
                                            }
                                        }
                                    ]
                                },
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/users/?page=0&size=10&sort=username,asc"
                                    }
                                },
                                "page": {
                                    "size": 10,
                                    "totalElements": 2,
                                    "totalPages": 1,
                                    "number": 0
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "_embedded": {
                                    "users": [
                                        {
                                            "username": "john",
                                            "accountNotLocked": true,
                                            "authority": "ROLE_USER",
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/users/john",
                                                    "title": "Get user"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/users/",
                                                    "title": "Modify user"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "accountNotLocked",
                                                            "required": true
                                                        },
                                                        {
                                                            "name": "authority",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "username",
                                                            "required": true,
                                                            "type": "text"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/users/"
                                                }
                                            }
                                        },
                                        {
                                            "username": "syedriadhhossen",
                                            "accountNotLocked": true,
                                            "authority": "ROLE_ADMINISTRATOR",
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                                    "title": "Get user"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/users/",
                                                    "title": "Modify user"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "accountNotLocked",
                                                            "required": true
                                                        },
                                                        {
                                                            "name": "authority",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "username",
                                                            "required": true,
                                                            "type": "text"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/users/"
                                                }
                                            }
                                        }
                                    ]
                                },
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/users/?page=0&size=10&sort=username,asc"
                                    }
                                },
                                "page": {
                                    "size": 10,
                                    "totalElements": 2,
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
                                "path": "/api/v1/users/"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/users/"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get pageable collection of users",
            description = "Returns a pageable collections of users with embedded Hypermedia links")
    @GetMapping("/")
    public ResponseEntity<?> usersPageable(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "sort", defaultValue = "username,asc") String... sort) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(new Sort.Order(Sort.Direction.fromOptionalString(sort[1]).orElse(Sort.DEFAULT_DIRECTION), sort[0]));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));

        Page<User> userPage = this.userService.users(pageable);

        if (userPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PagedModel<User> userPagedModel = this.userPagedResourcesAssembler.toModel(this.userService.users(pageable),
                this.userRepresentationAssembler);

        return new ResponseEntity<>(userPagedModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example({
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                 "_embedded": {
                                     "users": [
                                         {
                                             "username": "john",
                                             "accountNotLocked": false,
                                             "authority": "ROLE_USER",
                                             "_links": {
                                                 "self": {
                                                     "href": "http://localhost:8080/api/v1/users/john",
                                                     "title": "Get user"
                                                 },
                                                 "modify": {
                                                     "href": "http://localhost:8080/api/v1/users/",
                                                     "title": "Modify user"
                                                 }
                                             }
                                         },
                                         {
                                             "username": "syedriadhhossen",
                                             "accountNotLocked": true,
                                             "authority": "ROLE_ADMINISTRATOR",
                                             "_links": {
                                                 "self": {
                                                     "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                                     "title": "Get user"
                                                 },
                                                 "modify": {
                                                     "href": "http://localhost:8080/api/v1/users/",
                                                     "title": "Modify user"
                                                 }
                                             }
                                         }
                                     ]
                                 },
                                 "_links": {
                                     "self": {
                                         "href": "http://localhost:8080/api/v1/users/list",
                                         "title": "Get list of users"
                                     },
                                     "collection": {
                                         "href": "http://localhost:8080/api/v1/users/?page=0&size=10&sort=username%2Casc",
                                         "title": "Get pageable list of users"
                                     }
                                 }
                             }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "_embedded": {
                                    "users": [
                                        {
                                            "username": "john",
                                            "accountNotLocked": false,
                                            "authority": "ROLE_USER",
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/users/john",
                                                    "title": "Get user"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/users/",
                                                    "title": "Modify user"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "accountNotLocked",
                                                            "required": true
                                                        },
                                                        {
                                                            "name": "authority",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "username",
                                                            "required": true,
                                                            "type": "text"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/users/"
                                                }
                                            }
                                        },
                                        {
                                            "username": "syedriadhhossen",
                                            "accountNotLocked": true,
                                            "authority": "ROLE_ADMINISTRATOR",
                                            "_links": {
                                                "self": {
                                                    "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                                    "title": "Get user"
                                                },
                                                "modify": {
                                                    "href": "http://localhost:8080/api/v1/users/",
                                                    "title": "Modify user"
                                                }
                                            },
                                            "_templates": {
                                                "default": {
                                                    "method": "PUT",
                                                    "contentType": "application/json",
                                                    "properties": [
                                                        {
                                                            "name": "accountNotLocked",
                                                            "required": true
                                                        },
                                                        {
                                                            "name": "authority",
                                                            "readOnly": true
                                                        },
                                                        {
                                                            "name": "username",
                                                            "required": true,
                                                            "type": "text"
                                                        }
                                                    ],
                                                    "target": "http://localhost:8080/api/v1/users/"
                                                }
                                            }
                                        }
                                    ]
                                },
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/users/list",
                                        "title": "Get list of users"
                                    },
                                    "collection": {
                                        "href": "http://localhost:8080/api/v1/users/?page=0&size=10&sort=username%2Casc",
                                        "title": "Get pageable list of users"
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
                                "path": "/api/v1/users/list"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/users/list"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get list of users", description = "Returns a list of users with embedded Hypermedia links")
    @GetMapping("/list")
    public ResponseEntity<?> usersList() {
        List<User> users = this.userService.users();

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        CollectionModel<User> userCollectionModel = this.userRepresentationAssembler.toCollectionModel(users);

        return new ResponseEntity<>(userCollectionModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "username": "syedriadhhossen",
                                "accountNotLocked": true,
                                "authority": "ROLE_ADMINISTRATOR",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                        "title": "Get user"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/users/",
                                        "title": "Modify user"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "username": "syedriadhhossen",
                                "accountNotLocked": true,
                                "authority": "ROLE_ADMINISTRATOR",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                        "title": "Get user"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/users/",
                                        "title": "Modify user"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "accountNotLocked",
                                                "required": true
                                            },
                                            {
                                                "name": "authority",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "username",
                                                "required": true,
                                                "type": "text"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/users/"
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
                                "path": "/api/v1/users/test"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/users/test"
                            }
                            """)
            }))
    })
    @Operation(method = "GET", summary = "Get a user by username", description = "Returns a user by the given username in the path variable")
    @GetMapping("/{username}")
    public ResponseEntity<?> user(@PathVariable("username") String username) {
        Optional<User> user = this.userService.user(username);
        if (user.isPresent()) {
            User userModel = this.userRepresentationAssembler.toModel(user.get());
            return new ResponseEntity<>(userModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaTypes.HAL_JSON_VALUE, value = """
                            {
                                "username": "syedriadhhossen",
                                "accountNotLocked": true,
                                "authority": "ROLE_ADMINISTRATOR",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                        "title": "Get user"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/users/",
                                        "title": "Modify user"
                                    }
                                }
                            }
                            """),
                    @ExampleProperty(mediaType = MediaTypes.HAL_FORMS_JSON_VALUE, value = """
                            {
                                "username": "syedriadhhossen",
                                "accountNotLocked": true,
                                "authority": "ROLE_ADMINISTRATOR",
                                "_links": {
                                    "self": {
                                        "href": "http://localhost:8080/api/v1/users/syedriadhhossen",
                                        "title": "Get user"
                                    },
                                    "modify": {
                                        "href": "http://localhost:8080/api/v1/users/",
                                        "title": "Modify user"
                                    }
                                },
                                "_templates": {
                                    "default": {
                                        "method": "PUT",
                                        "contentType": "application/json",
                                        "properties": [
                                            {
                                                "name": "accountNotLocked",
                                                "required": true
                                            },
                                            {
                                                "name": "authority",
                                                "readOnly": true
                                            },
                                            {
                                                "name": "username",
                                                "required": true,
                                                "type": "text"
                                            }
                                        ],
                                        "target": "http://localhost:8080/api/v1/users/"
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
                                "message": "User not found with username: test",
                                "error": "Bad Request",
                                "path": "/api/v1/users/"
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
                                "path": "/api/v1/users/"
                            }
                            """),
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-03 10:38:23 PM",
                                "message": "Unauthorized",
                                "error": "Access token expired",
                                "path": "/api/v1/users/"
                            }
                            """)
            })),
            @ApiResponse(code = 403, message = "Forbidden", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 403,
                                "timestamp": "2022-11-03 11:04:25 PM",
                                "message": "You do not have permission to access this resource",
                                "error": "Forbidden",
                                "path": "/api/v1/users/"
                            }
                            """)
            }))
    })
    @Operation(method = "PUT", summary = "Update a user", description = "Updates and returns a user using the validated request body. " +
            "Only administrator roles are authorized to perform this action")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATOR')")
    @PutMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@Valid @RequestBody User user) {
        User modifiedUser = this.userService.update(user);
        User userModel = this.userRepresentationAssembler.toModel(modifiedUser);

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }
}
