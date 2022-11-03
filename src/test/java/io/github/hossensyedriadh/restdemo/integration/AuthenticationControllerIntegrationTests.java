package io.github.hossensyedriadh.restdemo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.AccessTokenRequest;
import io.github.hossensyedriadh.restdemo.configuration.authentication.model.BearerTokenRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
public class AuthenticationControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void when_access_authentication_with_credentials_throw_BadRequest() throws Exception {
        BearerTokenRequest bearerTokenRequest = new BearerTokenRequest();
        bearerTokenRequest.setUsername("test");
        bearerTokenRequest.setPassphrase("password");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/authentication/")
                        .content(new ObjectMapper().writeValueAsBytes(bearerTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(System.out::println).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void when_access_renew_access_token_with_refresh_token_throw_unauthorized() throws Exception {
        AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setRefresh_token("""
                eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJSZWZyZXNoIFRva2VuIiwibmJmIjoxNj
                        Y3NDY3MDI2LCJhdXRob3JpdHkiOiJST0xFX0FETUlOSVNUUkFUT1IiLCJleHAiOjE2Njc0Nzc4MjYsImlhdCI6MTY2NzQ2NzA
                        yNiwianRpIjoiZmE4OTY3ZjMtZmI1ZS00MDVkLWE5MTEtNDM2ZTdhOGZhZjU4IiwidXNlcm5hbWUiOiJzeWVkcmlhZGhob3Nz
                        ZW4ifQ.B4vIK2P0zItoJdaSQ5BFQuJYOgNxNQ5sxosIjiCin18LUDH4bqYYMA7ligfNaunLo13fMR8tDD5leZ2TTV3677X2fN
                        2225SCVkEqpTRXk-8m4XTICu_1KaojK3rn80AeUqCeGvhw01Ar65siHyEdbPrVj7WQcHnQ4lt_u-j-uUNFnPtdLkGUMrvAuUv
                        SGPPqAi5bmfBO4dTt4OP7vtc7rewLr88QsttChYgO4lXgbwl4BuDS26_NJHF6pMQvw7d0rdCXUYqxiNL6e5HmV7nbK0UrsYLF
                        g6_1Vd73pUd28t_kH98Ft_eXfFCfZx1kJ6WxdG3T6SlNyMDCHGrwZw41mQ
                """);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/authentication/access-token")
                        .content(new ObjectMapper().writeValueAsBytes(accessTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(System.out::println).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
