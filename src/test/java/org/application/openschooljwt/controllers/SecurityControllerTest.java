package org.application.openschooljwt.controllers;

import org.application.openschooljwt.services.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "USER")
    public void givenRoleUser_whenGetToUser_thenReturnStatusOk() throws Exception {
        mockMvc.perform(get("/private/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("Вы пользователь"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void givenRoleAdmin_whenGetToAdmin_thenReturnStatusOk() throws Exception {
        mockMvc.perform(get("/private/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Вы администратор"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void givenRoleUser_whenGetToAdmin_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/private/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenNoAuthUser_whenGetUserUrl_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/private/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenNoAuthUser_whenGetAdminUrl_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/private/admin"))
                .andExpect(status().isForbidden());
    }
}
