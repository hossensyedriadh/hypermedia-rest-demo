package io.github.hossensyedriadh.restdemo.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    public void whenAccessAnonymously_throwUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/posts"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void whenAccessPostsPageableAuthenticated_return_2xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/posts/"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void whenAccessPostsPageableAuthenticated_with_AcceptAPPLICATIONJSON_throwNotAcceptable() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/posts/").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    public void whenAccessPostsPageableAuthenticated_with_AcceptHALJSON_return_2xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/posts/").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void whenAccessPost_with_AcceptHALJSON_return_2xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/posts/{id}", UUID.randomUUID().toString())
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void whenAccessPost_with_AcceptAPPLICATIONJSON_throwNotAcceptable() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/posts/{id}", UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    public void whenAccessCreateAuthenticated_with_MediaTypeAPPLICATIONJSON_throwBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void whenAccessDeleteAuthenticated_throwMethodNotAllowed() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/v1/posts/"))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser
    public void whenAccessDeleteAuthenticated_throwBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/v1/posts/{id}", UUID.randomUUID().toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
