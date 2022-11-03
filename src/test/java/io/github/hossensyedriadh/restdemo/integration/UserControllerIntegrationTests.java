package io.github.hossensyedriadh.restdemo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hossensyedriadh.restdemo.entity.User;
import io.github.hossensyedriadh.restdemo.enumerator.Authority;
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

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    public void whenAccessAnonymously_throwUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void whenAccessPageableAuthenticated_return2xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void whenAccessPageableAuthenticatedWithAcceptXML_returnNotAcceptable() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/").accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }

    @Test
    @WithMockUser
    public void whenAccessPageableAuthenticatedWithAcceptHALFORMSJSON_return2xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/").accept(MediaTypes.HAL_FORMS_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void whenAccessPageableAuthenticatedWithAcceptHALJSON_return2xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMINISTRATOR"})
    public void whenAccessUpdateAuthenticatedUsingROLEUSER_withContentTypeJSON_returnBadRequest() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setAccountNotLocked(true);
        user.setAuthority(Authority.ROLE_USER);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/v1/users/").content(new ObjectMapper().writeValueAsBytes(user))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void whenAccessUserAuthenticated_usingMethodGET_return_2xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/{username}", "test")
                .accept(MediaTypes.HAL_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void whenAccessUserAuthenticated_usingMethodDELETE_throw_4xx() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/"))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }
}
