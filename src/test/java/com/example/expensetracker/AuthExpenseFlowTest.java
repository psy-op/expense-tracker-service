package com.example.expensetracker;

import com.example.expensetracker.dto.ExpenseCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthExpenseFlowTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void registerLoginAndCreateExpense() throws Exception {
        String registerJson = "{\"name\":\"Test User\",\"email\":\"t@example.com\",\"password\":\"secret123\"}";
        String token = mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(registerJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String jwt = mapper.readTree(token).get("token").asText();

        ExpenseCreateRequest req = new ExpenseCreateRequest();
        req.setDescription("Coffee");
        req.setAmount(new BigDecimal("3.50"));
        req.setDate(LocalDate.now());

        mvc.perform(post("/api/expenses")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Coffee"));
    }
}
