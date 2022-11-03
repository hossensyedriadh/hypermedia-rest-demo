package io.github.hossensyedriadh.restdemo.controller.v1.authentication;

import io.github.hossensyedriadh.restdemo.configuration.authentication.model.AccessTokenRequest;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.BearerTokenRequest;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.BearerTokenResponse;
import io.github.hossensyedriadh.restdemo.service.authentication.AuthenticationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/authentication", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJBY2Nlc3MgVG9rZW4iLCJuYmYiOjE2Njc
                                    0NjcwMjYsImF1dGhvcml0eSI6IlJPTEVfQURNSU5JU1RSQVRPUiIsImV4cCI6MTY2NzQ2ODgyNiwiaWF0IjoxNjY3NDY3MDI2
                                    LCJ1c2VybmFtZSI6InN5ZWRyaWFkaGhvc3NlbiJ9.E7Z9gmCriqfOhg3Vl2fIO-MluvRCmdc373c7Rvx-mQ__Eanav75NCsgg
                                    ulMK6LSb_jvchqsaI9kAI44VUBZSrYmJupmjxr3w7Lq4-JMSzKpbSnnvGM7ICHG7CO_wK47wGfV12WU7rBWQswfjlnwkKMv6w
                                    WUokiB0k3GfgbFvWL4b687otTe89XNESkTJrNYbI80EMtEyo44tFf30KU9HsYXxeuoMJSM2ffQQNb80DIcy0-CRn-0wEBW9tp
                                    6rvfgIp4kLiQqLVT3MieJgx6Eh6nvB26VxbZhal0TFo9uMu6kLCTQ2OeXwt9FF2_Ycg6pkphyLybqAruBprXmMWF9KWw",
                                "access_token_type": "Bearer",
                                "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJSZWZyZXNoIFRva2VuIiwibmJmIjoxNj
                                    Y3NDY3MDI2LCJhdXRob3JpdHkiOiJST0xFX0FETUlOSVNUUkFUT1IiLCJleHAiOjE2Njc0Nzc4MjYsImlhdCI6MTY2NzQ2NzA
                                    yNiwianRpIjoiZmE4OTY3ZjMtZmI1ZS00MDVkLWE5MTEtNDM2ZTdhOGZhZjU4IiwidXNlcm5hbWUiOiJzeWVkcmlhZGhob3Nz
                                    ZW4ifQ.B4vIK2P0zItoJdaSQ5BFQuJYOgNxNQ5sxosIjiCin18LUDH4bqYYMA7ligfNaunLo13fMR8tDD5leZ2TTV3677X2fN
                                    2225SCVkEqpTRXk-8m4XTICu_1KaojK3rn80AeUqCeGvhw01Ar65siHyEdbPrVj7WQcHnQ4lt_u-j-uUNFnPtdLkGUMrvAuUv
                                    SGPPqAi5bmfBO4dTt4OP7vtc7rewLr88QsttChYgO4lXgbwl4BuDS26_NJHF6pMQvw7d0rdCXUYqxiNL6e5HmV7nbK0UrsYLF
                                    g6_1Vd73pUd28t_kH98Ft_eXfFCfZx1kJ6WxdG3T6SlNyMDCHGrwZw41mQ"
                            }
                            """)
            })),
            @ApiResponse(code = 401, message = "Unauthorized", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-01 10:58:36 PM",
                                "message": "Invalid credentials",
                                "error": "Unauthorized",
                                "path": "/api/v1/authentication/"
                            }
                            """)
            })),
            @ApiResponse(code = 403, message = "Forbidden", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 403,
                                "timestamp": "2022-11-03 11:04:25 PM",
                                "message": "User account locked: john",
                                "error": "Forbidden",
                                "path": "/api/v1/authentication/"
                            }
                            """)
            }))
    })
    @Operation(method = "POST", summary = "Get authentication tokens using username and password",
            description = "Returns access token, access token type and refresh token upon authentication using username and password")
    @PostMapping("/")
    public ResponseEntity<?> authenticate(@RequestBody BearerTokenRequest bearerTokenRequest) {
        BearerTokenResponse bearerTokenResponse = this.authenticationService.authenticate(bearerTokenRequest);
        return new ResponseEntity<>(bearerTokenResponse, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJBY2Nlc3MgVG9rZW4iLCJuYmYiOjE2Njc
                                    0NjcwMjYsImF1dGhvcml0eSI6IlJPTEVfQURNSU5JU1RSQVRPUiIsImV4cCI6MTY2NzQ2ODgyNiwiaWF0IjoxNjY3NDY3MDI2
                                    LCJ1c2VybmFtZSI6InN5ZWRyaWFkaGhvc3NlbiJ9.E7Z9gmCriqfOhg3Vl2fIO-MluvRCmdc373c7Rvx-mQ__Eanav75NCsgg
                                    ulMK6LSb_jvchqsaI9kAI44VUBZSrYmJupmjxr3w7Lq4-JMSzKpbSnnvGM7ICHG7CO_wK47wGfV12WU7rBWQswfjlnwkKMv6w
                                    WUokiB0k3GfgbFvWL4b687otTe89XNESkTJrNYbI80EMtEyo44tFf30KU9HsYXxeuoMJSM2ffQQNb80DIcy0-CRn-0wEBW9tp
                                    6rvfgIp4kLiQqLVT3MieJgx6Eh6nvB26VxbZhal0TFo9uMu6kLCTQ2OeXwt9FF2_Ycg6pkphyLybqAruBprXmMWF9KWw",
                                "access_token_type": "Bearer",
                                "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJSZWZyZXNoIFRva2VuIiwibmJmIjoxNj
                                    Y3NDY3MDI2LCJhdXRob3JpdHkiOiJST0xFX0FETUlOSVNUUkFUT1IiLCJleHAiOjE2Njc0Nzc4MjYsImlhdCI6MTY2NzQ2NzA
                                    yNiwianRpIjoiZmE4OTY3ZjMtZmI1ZS00MDVkLWE5MTEtNDM2ZTdhOGZhZjU4IiwidXNlcm5hbWUiOiJzeWVkcmlhZGhob3Nz
                                    ZW4ifQ.B4vIK2P0zItoJdaSQ5BFQuJYOgNxNQ5sxosIjiCin18LUDH4bqYYMA7ligfNaunLo13fMR8tDD5leZ2TTV3677X2fN
                                    2225SCVkEqpTRXk-8m4XTICu_1KaojK3rn80AeUqCeGvhw01Ar65siHyEdbPrVj7WQcHnQ4lt_u-j-uUNFnPtdLkGUMrvAuUv
                                    SGPPqAi5bmfBO4dTt4OP7vtc7rewLr88QsttChYgO4lXgbwl4BuDS26_NJHF6pMQvw7d0rdCXUYqxiNL6e5HmV7nbK0UrsYLF
                                    g6_1Vd73pUd28t_kH98Ft_eXfFCfZx1kJ6WxdG3T6SlNyMDCHGrwZw41mQ"
                            }
                            """)
            })),
            @ApiResponse(code = 401, message = "Unauthorized", examples = @Example(value = {
                    @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = """
                            {
                                "status": 401,
                                "timestamp": "2022-11-01 11:03:12 PM",
                                "message": "Invalid refresh token",
                                "error": "Unauthorized",
                                "path": "/api/v1/authentication/access-token"
                            }
                            """)
            }))
    })
    @Operation(method = "POST", summary = "Get new access token",
            description = "Returns a new access token upon successful validation of the refresh token in the request body")
    @PostMapping("/access-token")
    public ResponseEntity<?> renewAccessToken(@RequestBody AccessTokenRequest accessTokenRequest) {
        BearerTokenResponse bearerTokenResponse = this.authenticationService.renewAccessToken(accessTokenRequest);
        return new ResponseEntity<>(bearerTokenResponse, HttpStatus.OK);
    }
}
