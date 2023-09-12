package io.oopy.coding.global.jwt.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class JwtExceptionFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthorizedExceptionHandling() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/login")
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code").value("Unauthorized"))
                .andExpect(jsonPath("$.message").exists());
    }
}